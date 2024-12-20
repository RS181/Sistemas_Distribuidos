package ds.assign.tom;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Logger;

class QueueMonitor implements Runnable {

    // Host and Port of the peer we are monitoring
    private String host;
    private int port;

    // Total number of peer's in the network 
    private int numberOfPeers;

    //
    private final PriorityQueue<String> queue;
    private final Logger logger;

    private String outputFilePath;

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

    @Override
    public void run() {
        try {
            logger.info("QueueMonitor : endpoint running at port " + port + " ...");

            while (true) {
                synchronized (queue) {

                    // check message at the from of the queue
                    String m = queue.peek();
                    
                    // check if queue has messages from all other peer's
                    if (existsMessageFromAllPeers()){
                        logger.info("QueueMonitor " + host + " @" + port + " has messages from all other peer's");
                        
                        queue.poll();
                        if (m.contains("ACK")){
                            // discard m
                        }
                        else{ 
                            // deliver m to Pj (in this case we print m to a file)
                            try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true));){
                                String word = m.split(":")[0];
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
                Thread.sleep(100); // Verifica a cada 1ms
            }
        } catch (Exception e) {
            logger.warning("QueueMonitor interrupted.");
        }
    }


    /**
     * Checks if a message from all Peer's exist's in queue (ACK or normal message)
     * @return
     */
    private Boolean existsMessageFromAllPeers(){
        
        Set<String> s = new HashSet<>();
        
        for (String msg : queue){
            String [] parts = msg.split(":");

            //peerInfo = host:port
            String peerInfo = parts[parts.length-2] + ":" + parts[parts.length-1];
            s.add(peerInfo);
            if (s.size() == numberOfPeers)
                return true;
        }

        return false;
    }


    private void cleanOutputTextFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, false));){
            writer.write("");
        }
        catch(Exception e ){
            logger.warning("QueueMonitor: Error ocured while print mesage to file");
        }
    }

    
}
