package ds.assign.tom;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;


/**
 * Class that is reponsable for sending a message (data or ack) to every peer (including itself)
 */
public class MultiCast implements Runnable{

    // Peer that is going to send message
    Peer currentPeer;
    Logger logger;

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
        
    }

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


    public void sendAck(){
        List <PeerConnection> neighbours = currentPeer.neighbours;
        String senderTag = ":" + currentPeer.host + ":" + currentPeer.port;
        for (PeerConnection p : neighbours){
            // Send a Ack to peer server
            String host = p.getHost();
            int port = p.getPort();
            // logger.info("DEBUG: sending ACK to " + host + " " + port);
            sendRequestToServer("ACK:"+ counter + senderTag, host, port);
        }

        // logger.info("DEBUG: sending ACK to " +currentPeer.host +" " + currentPeer.port);
        // Send a message to itself
        sendRequestToServer("ACK:"+ counter + senderTag, currentPeer.host,Integer.parseInt(currentPeer.port));

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
                //  Thread.sleep(10000);

                String request = "TESTE";

                //just to test send of  one message 
                if (counter < 1 && !currentPeer.port.equals("20000") && !currentPeer.port.equals("30000") && !currentPeer.port.equals("40000") && !currentPeer.port.equals("50000") && !currentPeer.port.equals("60000")){
                    synchronized (request){
                        sendData(request + ":" +counter);
                        counter++;
                    }
                }



            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }

    }
    
}
