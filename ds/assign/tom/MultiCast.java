package ds.assign.tom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;



/**
 * Class that is reponsable for sending a message (data or ack) to every peer (including itself)
 */
public class MultiCast implements Runnable{

    // Peer that is going to send message
    private Peer currentPeer;
    private Logger logger;
    private LamportClock lamportClock;

    
    private ArrayList<String> wordsList;


    int counter = 0;


    private  PoissonProcess poissonProcess = null;



    public MultiCast (Peer peer ){
        this.currentPeer = peer;
        this.logger = peer.logger;

        //Intialize PoissonProcess
        Random rng = new Random();
        // TODO frequency of 1 per second
        double lambda = 4.0;   // 4 events per minute
        poissonProcess = new PoissonProcess(lambda, rng);

        wordsList = new ArrayList<>();
        loadWordsFromFile("ds/assign/tom/dictionary.txt");

        lamportClock = new LamportClock(0);
    }



    public LamportClock getLamportClock() {
        return lamportClock;
    }


    /**
     * Send's a message to all neighbour peer's (including itself)
     * @param msg
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
     * Send Acknowledge's regarding to a certain message to all neighbour peer's (including itself).
     * @param msg
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

        // logger.info("DEBUG: sending ACK to " +currentPeer.host +" " + currentPeer.port);
        // Send a message to itself
        sendRequestToServer("ACK:"+ msg + ":" + lamportClock.getTime() + senderTag, currentPeer.host,Integer.parseInt(currentPeer.port));

    }


    private void sendRequestToServer(String request, String serverHost, int serverPort){
        try {

            Socket socket = new Socket(serverHost,serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println(request);
            out.flush();
            
            socket.close();

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override   
    public void run() {
        logger.info("Multicast : endpoint running at port " + currentPeer.port + " ...");

        while (true) {
            double intervalTime = poissonProcess.timeForNextEvent() * 1000 * 60; // Converting to milliseconds

            try {
            
                Thread.sleep((long)intervalTime);

                // FOR EASY TESTING (REMOVE LATER)
                Thread.sleep(10000);

                String request = generateRandomRequest();

                //just to test send of  one message (by each Peer)
                if (counter < 1 ){
                    synchronized (request){
                        // update local Lamport clock
                        lamportClock.increment();
                        
                        // build message with timestamp
                        String msg = request + ":" + lamportClock.getTime();

                        // send to all neighour peers
                        sendData(msg);


                        //REMOVER MAIS TARDE 
                        counter++;
                    }
                }



            } catch (Exception e) {
                //e.printStackTrace();
                logger.warning("Muticast: Error ocurred in "+ currentPeer.host + " " + currentPeer.port);
            }

        }

    }


    private void loadWordsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) 
                wordsList.add(line.trim());
            
            //logger.info("FINISHED LOADING DICTIONARY OF WORD'S");
        } catch (Exception e) {
            logger.warning("ERROR ocured while loading word's from dictionary");
            // e.printStackTrace();
        }
    }

    private String generateRandomRequest() {
        Random random = new Random();
        String newWord = wordsList.get(random.nextInt(wordsList.size()));

        String request = String.format("%s", newWord);

        return request;
    }
    
}
