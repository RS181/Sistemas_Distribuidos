package ds.assign.tom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.logging.Logger;



/**
 * The MultiCast class is responsible for sending messages (both data and acknowledgments) to all peers 
 * in a peer-to-peer network, including itself. It uses a Poisson process to determine the timing of 
 * message transmissions, ensuring that messages are sent at a controlled rate.
 * 
 * <p>This class runs continuously in a separate thread and generates random requests to be sent to peers,
 * while also managing the sending of acknowledgments in response to these messages.</p>
 * 
 * @see <a href="https://github.com/RS181/">Repository</a>
 * @author Rui Santos
 */
public class MultiCast implements Runnable{

    // Peer that is going to send message
    private Peer currentPeer;
    private Logger logger;
    private LamportClock lamportClock;
    private PriorityQueue <String> queue;

    // List of words loaded from a dictionary file
    private ArrayList<String> wordsList;
    private  PoissonProcess poissonProcess = null;

    /**
     * Constructs a MultiCast instance for the given Peer. Initializes the Poisson process for event 
     * generation, loads the dictionary of words, and initializes the Lamport clock.
     *
     * @param peer the Peer instance that will be responsible for sending messages
     */
    public MultiCast (Peer peer ){
        this.currentPeer = peer;
        this.logger = peer.logger;

        //Intialize PoissonProcess
        Random rng = new Random();
        double lambda = 60.0;   //60 events per minute (ESTE CAUSA ALGUNS PROBLEMAS)
        poissonProcess = new PoissonProcess(lambda, rng);

        // Load words from dictionary file
        wordsList = new ArrayList<>();
        loadWordsFromFile("ds/assign/tom/dictionary.txt");

        // Initialize Lamport clock
        lamportClock = new LamportClock(0);
    }

    public void setQueue(PriorityQueue<String> queue) {
        this.queue = queue;
    }


    public LamportClock getLamportClock() {
        return lamportClock;
    }


    /**
     * Sends a data message to all neighboring peers, including itself.
     * 
     * @param msg the message to be sent
     */
    private void sendData(String msg){
        List <PeerConnection> neighbours = currentPeer.neighbours;
        String senderTag = ":" + currentPeer.host + ":" + currentPeer.port;
        for (PeerConnection p : neighbours){
            // Send a message to peer server
            String host = p.getHost();
            int port = p.getPort();
            sendRequestToServer(msg + senderTag, host, port);
        }

        // Send message to itself 
        sendRequestToServer(msg + senderTag,currentPeer.host, Integer.parseInt(currentPeer.port));
    }

    /**
     * Sends an acknowledgment message regarding a specific message to all neighboring peers, including itself.
     * 
     * @param msg the original message to acknowledge
     */
    public void sendAck(String msg){
        List <PeerConnection> neighbours = currentPeer.neighbours;
        String senderTag = ":" + currentPeer.host + ":" + currentPeer.port;
        for (PeerConnection p : neighbours){
            // Send a Ack to peer server
            String host = p.getHost();
            int port = p.getPort();
            // logger.info("DEBUG: sending ACK to " + host + " " + port);
            sendRequestToServer("ACK:"+ msg + ":" + lamportClock.getTime() + senderTag, host, port);
        }

        // Send a message to itself
        sendRequestToServer("ACK:"+ msg + ":" + lamportClock.getTime() + senderTag, currentPeer.host,Integer.parseInt(currentPeer.port));

    }

    /**
     * Sends a request message to Peer server located at the specified host and port.If a peer's 
     * has disconected we exit the program (this allows for easier 'stop' of every peer)
     * 
     * @param request the message to be sent
     * @param serverHost the host address of the server
     * @param serverPort the port of the server
     */
    private void sendRequestToServer(String request, String serverHost, int serverPort){
        try {

            Socket socket = new Socket(serverHost,serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println(request);
            out.flush();
            
            socket.close();

            
        } catch (Exception e) {

            logger.severe("MultiCast: " + serverHost + " " + serverPort + "is OFFLINE. Shutting down...");
            System.exit(0);
            //e.printStackTrace();
        }
    }

    /**
     * The run method continuously generates messages using the Poisson process and sends them to all peers.
     */
    @Override   
    public void run() {
        logger.info("Multicast : endpoint running at port " + currentPeer.port + " ...");

        while (true) {
            double intervalTime = poissonProcess.timeForNextEvent() * 1000 * 60; // Converting to milliseconds

            try {
            
                Thread.sleep((long)intervalTime);

                String request = generateRandomRequest();

                synchronized (queue){
                    // Update local Lamport clock
                    lamportClock.increment();
                    
                    // Build message with timestamp
                    String msg = request + ":" + lamportClock.getTime();
                    // Send to all neighour peers
                    sendData(msg);
                }

            } catch (Exception e) {
                logger.warning("Muticast: Error ocurred in "+ currentPeer.host + " " + currentPeer.port);
            }

        }

    }

    /**
     * Loads words from a dictionary file to be used for generating random messages.
     * 
     * @param filePath the path to the dictionary file
     */
    private void loadWordsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) 
                wordsList.add(line.trim());
            
            //logger.info("FINISHED LOADING DICTIONARY OF WORD'S");
        } catch (Exception e) {
            logger.warning("ERROR ocured while loading word's from dictionary");
        }
    }

    /**
     * Generates a random request message from the dictionary of words.
     * 
     * @return a randomly selected word
     */
    private String generateRandomRequest() {
        Random random = new Random();
        String newWord = wordsList.get(random.nextInt(wordsList.size()));

        String request = String.format("%s", newWord);

        return request;
    }
    
}
