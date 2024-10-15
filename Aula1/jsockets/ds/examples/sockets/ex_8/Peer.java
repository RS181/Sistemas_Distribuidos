package ds.examples.sockets.ex_8;

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


/**
 * 
 */
public class Peer {
    String host;
    Logger logger;
    String token = ""; // Representa o token, inicialmente vazio

    public Peer(String hostname, boolean hasToken) {
        host = hostname;
        logger = Logger.getLogger("logfile");
        token = hasToken ? "Token" : ""; // O peer inicializa com o token, ou não
        try {
            FileHandler handler = new FileHandler("./" + hostname + "_peer.log", true);
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
     * args[2] -> true/false of this starting with token
     * args[3] -> hostname of Peer we want to connect
     * args[4] -> port of Peer we want to connect
     * e.g 
     *  t1$ java Peer localhost 5000 true localhost 6000
     *  t2$ java Peer localhost 6000 false localhost 5000
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        boolean hasToken = args[2].equals("true"); // Define se o peer inicia com o token
        Peer peer = new Peer(args[0], hasToken);
        System.out.printf("new peer @ host=%s, hasToken=%s\n", args[0], hasToken);
        new Thread(new Server(args[0], Integer.parseInt(args[1]), peer)).start();
        new Thread(new Client(args[0], peer, args[3], Integer.parseInt(args[4]))).start(); // Passa a porta e o host do
                                                                                           // outro peer
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
                        logger.info("server: received token from client");
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
    String host;
    Peer peer;
    String otherPeerHost;
    int otherPeerPort;
    Logger logger;

    Boolean peerNotConnectedWarning = false;

    public Client(String host, Peer peer, String otherPeerHost, int otherPeerPort) {
        this.host = host;
        this.peer = peer;
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
                    // O peer tem o token, realiza a operação
                    logger.info("client @" + host + " has the token. Performing operation.");

                    // Simula alguma operação
                    Thread.sleep(5000); // Tempo da operação
                    logger.info("client @" + host + " completed operation. Sending token to peer.\n");

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
}
