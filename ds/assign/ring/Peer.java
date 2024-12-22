package ds.assign.ring;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import ds.assign.ring.RequestGenerator;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Represents a single Peer in a token ring network.
 * The Peer acts as both a client and a server, communicating with other peers
 * and performing operations in case it has token.
 *
 * @see <a href="https://github.com/RS181/">Repository</a>
 * @author Rui Santos
 */
public class Peer {
    String host;
    String port;
    Logger logger;
    String token = ""; 

    /**
     * Constructs a Peer instance.
     *
     * @param host the hostname of this Peer
     * @param port the port of this Peer
    */
    public Peer(String host, String port) {
        this.host = host;
        this.port = port;
        logger = Logger.getLogger("logfile");
        try {
            FileHandler handler = new FileHandler("./ds/assign/ring/logs/" + host + "_peer_" + port + ".log", false);
            logger.addHandler(handler);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param args  args Command-line arguments:
     * args[0] -> this Peer hostname
     * args[1] -> this Peer port
     * args[2] -> hostname of Peer we want to connect
     * args[3] -> port of Peer we want to connect
     * args[4] -> hostname of calculator server
     * 
     * Example usage:
     * java Peer localhost 5000 localhost 6000
     * 
     * @throws Exception if an error occurs during execution
     */
    public static void main(String[] args) throws Exception {
        Peer peer = new Peer(args[0], args[1]);
        System.out.printf("new peer @ host=%s, Port =%s\n", args[0], args[1]);

        if (args.length < 5){
            System.out.println("Uncorrect format, use format: java Peer  <hostname_peer> <port_peer> <hostname_next_peer> <port_next_peer> <hostname_calculator_server>");
            return;
        }

        // Start this Peer as a Server
        Server server = new Server(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]),args[4] ,peer.logger);
        new Thread(server).start();

        // Start the RequestGenarator
        RequestGenerator requestGenerator = new RequestGenerator(args[0],Integer.parseInt(args[1]),peer.logger,server);
        new Thread(requestGenerator).start();
    }
}


/**
 * Server class representing the behavior of a Peer as a server.
 * Handles communication with other peers and forwards operations to the calculator server.
 */
class Server implements Runnable {
    // Atributes of current Peer
    ServerSocket server;
    String host;
    int port;
    Logger logger;
    String receivedToken;
    Queue<String> operations = new LinkedList<>();

    // Atributes of Peer we are going to connect
    String nextHost;
    int nextPort;

    //hostname of Calculator server (the port is always 44444)
    String calculatorServerAddress;
    

    /**
     * Constructs a Server instance.
     *
     * @param host the hostname of this Peer
     * @param port the port of this Peer
     * @param nextHost the hostname of the next Peer
     * @param nextPort the port of the next Peer
     * @param calculatorServerAddress the hostname of the calculator server
     * @param logger the logger instance for logging events
     * @throws Exception if an error occurs during server initialization
     */    
    public Server(String host, int port, String nextHost, int nextPort,String calculatorServerAddress ,Logger logger) throws Exception {
        this.host = host;
        this.port = port;
        this.logger = logger;
        this.nextHost = nextHost;
        this.nextPort = nextPort;
        this.calculatorServerAddress=calculatorServerAddress;
        server = new ServerSocket(port, 1, InetAddress.getByName(host));
    }

    /**
     * Add's operation to local server's Queue
     * 
     * @param operation operation the operation to be added
     */
    public void addOperations(String operation) {
        operations.add(operation);
    }

    @Override
    public void run() {
        try {
            logger.info("server: endpoint running at port " + port + " ...");
            final long TIMEOUT_MILLIS = 2 * 60 * 1000; // Time limit to wait (2 minutes) (I decided that 2 minutes was suficient)
            long startTime = System.currentTimeMillis();

            while (true) {
                logger.info("client @" + host + " waiting for token...");

                try {
                    // 1. Waits for connection (with time limit) 
                    server.setSoTimeout((int) (TIMEOUT_MILLIS - (System.currentTimeMillis() - startTime)));
                    Socket client = server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    receivedToken = in.readLine(); // Recebe o token

                    if (receivedToken.equals("Token")) {
                        logger.info("server: received token");
                    }

                    client.close();
                } catch (Exception e) {
                    logger.warning("Timeout: Token was not received in given time limit.\nPeer will Shutdown ...");
                    Thread.sleep(5000);
                    System.exit(0); // Encerrar o programa ou tomar outra ação
                }

                //2. Checks if current peer has token
                if (receivedToken.equals("Token")) {
                    startTime = System.currentTimeMillis(); // Reset time because it received token
           
                    synchronized(operations){

                        //2.1 Send all command's in operation to CalculatorMultiServer
                        while (!operations.isEmpty()){
                            String request = operations.poll();
                            String result = connectToCalculatorMultiServer(calculatorServerAddress, 44444, request);
                            logger.info("client @" + port + " RECEIVED result from server: " + result);
                        }

                        // Just to make the visualization more easy
                        Thread.sleep(5000);
                    }

                    // 2.2 Send token to neighbour peer
                    try {
                        Socket socket = new Socket(nextHost, nextPort);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("Token");
                        out.flush();
                        socket.close();
                        receivedToken = null; // reset token
                        logger.info("client @" + host + " sent the token to peer at port " + nextPort);
                    } catch (Exception e) {
                        System.out.printf("Connection between %s @%s and %s @%s FAILED\n", host, port, nextHost,
                                nextPort);
                    }
                } 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Connects to the calculator server and sends a command.
     *
     * @param serverHost the hostname of the calculator server
     * @param serverPort the port of the calculator server
     * @param command the command to be executed
     * @return the result of the command
     */
    public String connectToCalculatorMultiServer(String serverHost, int serverPort, String command) {
        String result = "";
        try {
            Socket socket = new Socket(InetAddress.getByName(serverHost), serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(command + ":" + port);
            out.flush();
            result = in.readLine();
            socket.close();
        } catch (Exception e) {
            logger.info("client @" + port + " failed to connect to calculator server.");
            e.printStackTrace();
        }
        return result;
    }
}
