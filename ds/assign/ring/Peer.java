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
import java.util.Random;

public class Peer {
    String host;
    String port;
    Logger logger;
    String token = ""; // Representa o token, inicialmente vazio

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
     * @param args
     *             args[0] -> this Peer hostname
     *             args[1] -> this Peer port
     *             args[2] -> hostname of Peer we want to connect
     *             args[3] -> port of Peer we want to connect
     *             e.g
     *             t1$ java Peer localhost 5000 localhost 6000
     *             t2$ java Peer localhost 6000 localhost 5000
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Peer peer = new Peer(args[0], args[1]);
        System.out.printf("new peer @ host=%s, Port =%s\n", args[0], args[1]);

        // Start this Peer as a Server
        new Thread(new Server(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]), peer.logger))
                .start();

        // Estabilishes connection from neighbour Peer (and send the current Peer as
        // input)
        //new Thread(new Client(peer, args[2], Integer.parseInt(args[3]))).start();
    }
}

/**
 * 
 */

class Server implements Runnable {
    // Atributes of current Peer
    ServerSocket server;
    String host;
    int port;
    Logger logger;
    String receivedToken;

    // Atributes of Peer we are going to connect
    String nextHost;
    int nextPort;

    public Server(String host, int port, String nextHost, int nextPort, Logger logger) throws Exception {
        this.host = host;
        this.port = port;
        this.logger = logger;
        this.nextHost = nextHost;
        this.nextPort = nextPort;
        server = new ServerSocket(port, 1, InetAddress.getByName(host));
    }

    @Override
    public void run() {
        try {
            logger.info("server: endpoint running at port " + port + " ...");
            while (true) {
                // O peer não tem o token, espera pelo token
                logger.info("client @" + host + " waiting for token...");
                
                //1. Waits for connection
                try {
                    Socket client = server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    receivedToken = in.readLine(); // Recebe o token

                    if (receivedToken.equals("Token")) {
                        logger.info("server: received token");
                    }

                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //2. Checks if current peer has token
                if (receivedToken.equals("Token")) {
                    

                    //2.1 Send command to CalculatorMultiServer
                    String result = connectToCalculatorMultiServer("localhost", 44444, generateRandomRequest());
                    logger.info("client @" + port + " RECEIVED result from server: " + result);

                    // Comentar no fim (so serve para ser mais facil de ver a troca de token)
                    Thread.sleep(5000);

                    // 2.2 Send token to neighbour peer
                    try {
                        Socket socket = new Socket(nextHost, nextPort);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                        out.println("Token");
                        out.flush();

                        socket.close();
                        // reset token
                        receivedToken = null;
                       

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
     * @param serverHost
     * @param serverPort
     * @param command
     * @return
     */
    public String connectToCalculatorMultiServer(String serverHost, int serverPort, String command) {
        String result = "";

        try {
            /*
             * create comunication Socket
             */
            Socket socket = new Socket(InetAddress.getByName(serverHost), serverPort);

            /*
             * prepare socket I/O channels
             */
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            /*
             * send command and port of this client
             */
            out.println(command + ":" + port);
            out.flush();

            /*
             * receive result
             */
            result = in.readLine();

            /*
             * close connection
             */
            socket.close();
        } catch (Exception e) {
            logger.info("client @" + port + " failed to connect to calculator server.");
            e.printStackTrace();

        }

        return result;
    }


    private String generateRandomRequest() {

        String[] operations = { "add", "sub", "mul", "div" };

        Random random = new Random();
        String operation = operations[random.nextInt(operations.length)];

        double x = Math.floor(random.nextDouble() * 100);
        double y = Math.floor(random.nextDouble() * 100);

        return operation + ":" + x + ":" + y;
    }

}
