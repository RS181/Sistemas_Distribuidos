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
            FileHandler handler = new FileHandler("./ds/assign/ring/logs/" + host + "_peer_"+port+".log", false);
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
     * args[0] -> this Peer hostname
     * args[1] -> this Peer port
     * args[2] -> hostname of Peer we want to connect
     * args[3] -> port of Peer we want to connect
     * e.g 
     *  t1$ java Peer localhost 5000  localhost 6000
     *  t2$ java Peer localhost 6000  localhost 5000
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Peer peer = new Peer(args[0],args[1]);
        System.out.printf("new peer @ host=%s, Port =%s\n", args[0],args[1]);

        // Start this Peer as a Server
        new Thread(new Server(args[0], Integer.parseInt(args[1]), peer)).start();

        // Estabilishes connection from neighbour Peer (and send the current Peer as input)
        new Thread(new Client(peer, args[2], Integer.parseInt(args[3]))).start(); 
    }
}

class Server implements Runnable {
    String host;
    int port;
    ServerSocket server;
    Peer peer; // Referência ao peer principal para controlar o token
    Logger logger;

    public Server(String host, int port, Peer peer) throws Exception {
        this.host = host;
        this.port = port;
        this.peer = peer;
        this.logger = peer.logger;
        server = new ServerSocket(port, 1, InetAddress.getByName(host));
    }

    @Override
    public void run() {
        try {
            logger.info("server: endpoint running at port " + port + " ...");
            while (true) {
                try {
                    Socket client = server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String receivedToken = in.readLine(); // Recebe o token

                    if ("Token".equals(receivedToken)) {
                        logger.info("server: received token");
                        peer.token = "Token"; // Atualiza o token no peer
                    }

                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Client implements Runnable {
    //Atributes of current Peer
    Peer peer;
    String host;
    String port;
      
    //Atributes of  Peer we are going to connect
    String otherPeerHost;
    int otherPeerPort;
    Logger logger;

    Boolean peerNotConnectedWarning = false;

    public Client(Peer peer, String otherPeerHost, int otherPeerPort) {
        this.peer = peer;
        this.host = peer.host;
        this.port = peer.port;
        this.otherPeerHost = otherPeerHost;
        this.otherPeerPort = otherPeerPort;
        this.logger = peer.logger;
    }

    @Override
    public void run() {
        try {
            logger.info("client: endpoint running ...\n");
            while (true) {
                if ("Token".equals(peer.token)) {
                    
                    if(sendCommandToCalculator()){
                        // O peer tem o token, realiza a operação
                        // logger.info("client @" + port + " SENT OP TO SERVER");
                        
                        // Envia o comando para o calculatorServer
                        String result = connectToCalculatorMultiServer("localhost", 44444, generateRandomRequest());
                        logger.info("client @" + port + " RECEIVED result from server: " + result);

                        // Após receber o resultado, continua com a troca de token
                        // logger.info("client @" + host + " COMPLETED OP,  Sending token to peer.\n");
                    
                        
                    }
                    
                    // Comentar no fim (so server para ser mais facil de ver troca de token)
                    Thread.sleep(5000);
                    
                    
                    // Envia o token para o outro peer
                    try {
                        Socket socket = new Socket(InetAddress.getByName(otherPeerHost), otherPeerPort);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("Token"); // Envia o token
                        out.flush();
                        socket.close();

                        // Após enviar, remove o token deste peer
                        peer.token = "";
                        logger.info("client @" + host + " sent the token to peer at port " + otherPeerPort);
                    } catch (IOException e) {
                        if (!peerNotConnectedWarning)
                            System.out.println("client [" + otherPeerHost + "] @" + otherPeerPort + " is OFFLINE");
                        peerNotConnectedWarning = true;
                    }
                } else {
                    // O peer não tem o token, espera pelo token
                    logger.info("client @" + host + " waiting for token...");
                    Thread.sleep(3000); // Espera antes de tentar verificar novamente
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
    public String connectToCalculatorMultiServer(String serverHost,int serverPort, String command){
        String result ="";

        try {
            /*
             * create comunication Socket 
             */
            Socket socket = new Socket(InetAddress.getByName(serverHost),serverPort);
            //logger.info("client @" + port + " connected to calculator server at " + serverHost + ":" + serverPort);
            
            /*
            * prepare socket I/O channels
            */
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         
            /*
            * send command and port of this client
            */
            out.println(command + ":" +port);
            out.flush();

            /*
            * receive result
            */
            result = in.readLine();
            //logger.info("client @" + port + " received result: " + result);

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

        String[] operations = {"add" , "sub" , "mul" , "div"};

        Random random = new Random();
        String operation = operations[random.nextInt(operations.length)];

        double x = Math.floor(random.nextDouble() * 100);
        double y = Math.floor(random.nextDouble() * 100);

        return operation + ":" + x + ":" + y;
    }
    

    /**
     * 
     * @return boolean value that decides if we are going to 
     * send a comand to the calculator server
     */
    static boolean sendCommandToCalculator(){
        return coinToss() == 1;
    }

    /**
     * 
     * @return 0 or 1 (simulating a coin toss)
     */
    static int coinToss(){
        return (int)Math.round(Math.random());
    }
}
