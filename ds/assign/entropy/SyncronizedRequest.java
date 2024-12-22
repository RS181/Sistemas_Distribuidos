package ds.assign.entropy;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;


/**
 * Handles synchronized requests in a peer-to-peer network using a Poisson process 
 * to determine event intervals. This class implements the Runnable interface to 
 * support concurrent execution.
 * 
 * @see <a href="https://github.com/RS181/">Repository</a>
 * @author Rui Santos
 */
public class SyncronizedRequest implements Runnable{

    private  PoissonProcess poissonProcess = null;

    //Origin Peer info 
    private final String host;
    private final int localPort;
    private final Logger logger;

    //Destination Peer info
    private  int destinationPort;
    private String destinationHost;
    private PeerConnection neighbourInfo;

    
    /**
     * Constructs a SyncronizedRequest instance with the specified parameters.
     *
     * @param host          the hostname of the local peer
     * @param localPort     the port of the local peer
     * @param neighbourInfo the PeerConnection object containing neighbor information
     * @param logger        the Logger for logging activity
     */
    public SyncronizedRequest(String host,int localPort,PeerConnection neighbourInfo , Logger logger){
        this.host = host;
        this.localPort = localPort;
        this.neighbourInfo = neighbourInfo;
        this.logger = logger;

        //Intialize PoissonProcess
        Random rng = new Random();
        double lambda = 2.0;    //2 event's per minute
        poissonProcess = new PoissonProcess(lambda, rng);

    }

    /**
     * Runs the synchronized request logic in a continuous loop. 
     * A random neighbor is selected periodically based on the Poisson process, 
     * and a synchronization request is sent to the selected peer.
     */
    @Override
    public void run() {
        logger.info("Started SyncronizedRequest on @"+localPort);

        while (true) {
            double intervalTime = poissonProcess.timeForNextEvent() * 1000 * 60; // Converting to milliseconds

            try {
                Thread.sleep((long)intervalTime);

                synchronized (neighbourInfo){
                    // Select a random neighbor for synchronization
                    String n = neighbourInfo.chooseRandomNeighbour();
                    
                    Scanner sc = new Scanner(n).useDelimiter(":");
                    destinationHost = sc.next();
                    destinationPort = Integer.parseInt(sc.next());
                }
                
                // Send sincronization request to Peer at destinationHost @destinationPort
                sendRequestToServer("SYNC-DATA");

            }catch (Exception e){
                e.printStackTrace(); 
            }

        }
    }
    
    /**
     * Sends a synchronization request to the destination peer.
     *
     * @param request the request message to send
     */
    public void sendRequestToServer(String request) {

        try{

            Socket socket = new Socket(InetAddress.getByName(destinationHost), destinationPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            out.println(request + ":" + localPort + ":" + host);
            out.flush();
            socket.close();

        } catch (Exception e){
            logger.warning("Server: error ocured while sending SYNC request to "+destinationHost+" " + destinationPort);
        }
    }
}
