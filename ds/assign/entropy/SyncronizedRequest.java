package ds.assign.entropy;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

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

    

    public SyncronizedRequest(String host,int localPort,PeerConnection neighbourInfo , Logger logger){
        this.host = host;
        this.localPort = localPort;
        this.neighbourInfo = neighbourInfo;
        this.logger = logger;

        //Intialize PoissonProcess
        Random rng = new Random();
        double lambda = 1.0;    //1 event per minute
        poissonProcess = new PoissonProcess(lambda, rng);

    }

    @Override
    public void run() {
        logger.info("Started SyncronizedRequest on @"+localPort);

        while (true) {
            double intervalTime = poissonProcess.timeForNextEvent() * 1000 * 60; // Converting to milliseconds

            try {
                Thread.sleep((long)intervalTime);

                
                synchronized (neighbourInfo){
                    //format: "hostname:port"
                    // choose a random neighbour peer to do Syncronization
                    String n = neighbourInfo.chooseRandomNeighbour();
                    
                    Scanner sc = new Scanner(n).useDelimiter(":");
                    destinationHost = sc.next();
                    destinationPort = Integer.parseInt(sc.next());
                }
            
                
                
                //send sincronization request to Peer at destinationHost @destinationPort
                sendRequestToServer("SYNC-DATA");

            }catch (Exception e){
                e.printStackTrace(); 
            }

        }
    }
    
    
    /**
     * We are using the localhost as server in all this exercises
     * @param request
     * @param serverport
     */
    public void sendRequestToServer(String request) {

        try{
            /*
             * make connection
             */
            Socket socket = new Socket(InetAddress.getByName(destinationHost), destinationPort);

			/*
			 * prepare socket output channel
			 */
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            
            /*
             * send syncronization request
             */

            out.println(request + ":" + localPort + ":" + host);
            out.flush();

            /*
             * close connection
             */
            socket.close();

        } catch (Exception e){
            //e.printStackTrace();
            logger.info("Server: error ocured while sending SYNC request to "+destinationHost+" " + destinationPort);
        }
    }
    
}
