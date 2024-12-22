package ds.assign.ring;
import java.util.Random;
import java.util.logging.Logger;
import java.net.Socket;
import java.net.InetAddress;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;



/**
 * A thread that generates random requests based on a Poisson process
 * and sends them to a given Peer. Acts as a client that generates operations
 * for processing.
 * 
 * This class is responsible for periodically creating and queuing operations
 * in the Peer server's queue.
 * 
 * @see <a href="https://github.com/RS181/">Repository</a>
 * @author Rui Santos
 */
public class RequestGenerator implements Runnable{

    private final String host;
    private final int localPort;
    private final Logger logger;
    private final Server server;
    private  PoissonProcess poissonProcess = null;


   
    /**
     * Constructs a RequestGenerator instance for a Peer.
     * 
     * @param host      the hostname of the Peer that owns this RequestGenerator
     * @param localPort the local port of the Peer
     * @param logger    the logger instance used by the Peer for logging events
     * @param server    the Peer server where requests will be queued
     */
    public RequestGenerator (String host,int localPort, Logger logger,Server server){
        this.host = host;
        this.localPort = localPort;
        this.logger = logger;
        this.server = server;

        //Intialize PoissonProcess with an average of 4 events per minute
        Random rng = new Random();
        double lambda = 4.0;   // 4 events per minute
        poissonProcess = new PoissonProcess(lambda, rng);
    }


    /**
     * Continuously generates random requests at intervals determined by a Poisson process.
     * Each request is added to the Peer server's operation queue.
     */
    @Override
    public void run() {
        while (true) {
            double intervalTime = poissonProcess.timeForNextEvent() * 1000 * 60; // Converting to milliseconds

            try {
                Thread.sleep((long)intervalTime);
                String request = generateRandomRequest();
                
                // Synchronize the request addition to avoid race conditions
                synchronized(request){
                    server.addOperations(request);
                }
            }catch (Exception e){
                e.printStackTrace(); 
            }
        }
    }

    /**
     * Generates a random mathematical operation request.
     * 
     * @return a request string in the format "operation:x:y", 
     *         where `operation` is one of {"add", "sub", "mul", "div"} 
     *         and `x` and `y` are randomly generated doubles.
     */
    private String generateRandomRequest() {

        String[] operations = {"add" , "sub" , "mul" , "div"};

        Random random = new Random();
        String operation = operations[random.nextInt(operations.length)];

        //Used Math.floor, to have more 'readable' numbers
        double x = Math.floor(random.nextDouble() * 100);
        double y = Math.floor(random.nextDouble() * 100);

        return  operation + ":" + x + ":" + y;
    }
}
