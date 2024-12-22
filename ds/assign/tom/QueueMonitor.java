package ds.assign.tom;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Logger;

/**
 * The QueueMonitor class monitors a message queue in a peer-to-peer network, ensuring that messages
 * from all peers are received before processing and delivering them in a totally ordered manner.
 * This class runs continuously and checks the queue for messages, and once messages from all peers
 * are available, it processes and writes the messages to an output file.
 * 
 * <p>The QueueMonitor ensures that the messages are processed in the order they are received,
 * maintaining the consistency of the system.</p>
 * 
 * @see <a href="https://github.com/RS181/">Repository</a>
 * @author Rui Santos
 */
class QueueMonitor implements Runnable {

    // Host and Port of the peer we are monitoring
    private String host;
    private int port;

    // Total number of peer's in the network 
    private int numberOfPeers;

    // Priority queue holding the messages to be processed
    private final PriorityQueue<String> queue;
    private final Logger logger;


    // Output file path where the message will be logged
    private String outputFilePath;


    /**
     * Constructs a QueueMonitor to monitor a message queue, check for messages from all peers, 
     * @param host the host of the peer this monitor is associated with
     * and deliver them by writing to an output file.
     * 
     * @param queue the priority queue containing the messages to be monitored
     * @param port the port of the peer this monitor is associated with
     * @param numberOfPeers the total number of peers in the network
     * @param logger the logger to log important events, warnings, and errors
     */
    public QueueMonitor(PriorityQueue<String> queue, String host, int port,int numberOfPeers, Logger logger) {
        this.queue = queue;
        this.host = host;
        this.port = port;
        this.numberOfPeers = numberOfPeers;
        this.logger = logger;
        this.outputFilePath = String.format("./ds/assign/tom/out/%s_%s_dic.txt", host, port);
        

        //Reset the file we are going to write the word's
        cleanOutputTextFile();

    }

    /**
     * The run method is executed when the QueueMonitor is started as a thread.
     * It continuously checks the queue for messages from all peers and processes them 
     * when they are available.
     */
    @Override
    public void run() {
        try {
            logger.info("QueueMonitor : endpoint running at port " + port + " ...");

            while (true) {
                synchronized (queue) {

                    // Check message at the from of the queue
                    String m = queue.peek();
                    
                    // Check if queue has messages from all other peer's
                    if (existsMessageFromAllPeers()){
                        logger.info("QueueMonitor " + host + " @" + port + " has messages from all other peer's");
                        
                        // Poll the message from the queue
                        queue.poll();

                        // If the message is an ACK, discard it
                        if (m.contains("ACK")){
                            // discard m
                        }
                        else{ 
                            // Deliver the message to Pj (in this case, write to a file)
                            try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true));){
                                String word = m.split(":")[0] + ":" + m.split(":")[1];
                                // writer.write(m);
                                writer.write(word);
                                writer.newLine();
                            }
                            catch(Exception e ){
                                logger.warning("QueueMonitor: Error ocured while print mesage to file");
                            }
                        }
                    }

                }
                Thread.sleep(300); //  Check every 300ms
            }
        } catch (Exception e) {
            logger.warning("QueueMonitor interrupted.");
        }
    }


    /**
     * Checks if a message from all peers (either ACK or normal message) exists in the queue.
     * 
     * @return true if a message from all peers is present in the queue, otherwise false
     */
    private Boolean existsMessageFromAllPeers(){
        
        Set<String> s = new HashSet<>();
        
        for (String msg : queue){
            String [] parts = msg.split(":");

            // Construct peer information as "host:port"
            String peerInfo = parts[parts.length-2] + ":" + parts[parts.length-1];
            s.add(peerInfo);
            if (s.size() == numberOfPeers)
                return true;
        }

        return false;
    }

    /**
     * Resets the output text file by clearing its content.
    */
    private void cleanOutputTextFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, false));){
            writer.write("");
        }
        catch(Exception e ){
            logger.warning("QueueMonitor: Error ocured while reseting file");
        }
    }

    
}
