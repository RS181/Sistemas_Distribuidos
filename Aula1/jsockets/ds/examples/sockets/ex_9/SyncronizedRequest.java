package ds.examples.sockets.ex_9;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;

public class SyncronizedRequest implements Runnable{

    private  PoissonProcess poissonProcess = null;

    //Origin Peer info 
    private final String host;
    private final int localPort;
    private final Logger logger;

    //Destination Peer info
    private final int destinationPort;
    

    public SyncronizedRequest(String host,int localPort, int destinationPort, Logger logger){
        this.host = host;
        this.localPort = localPort;
        this.destinationPort = destinationPort;
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

                //send sincronization request to Peer at destinationPort
                sendRequestToServer("SYNC-DATA", destinationPort,localPort);

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
    public void sendRequestToServer(String request, int serverport,int localport) {
        String serverAddress = "localhost";

        try{
            /*
             * make connection
             */
            Socket socket = new Socket(InetAddress.getByName(serverAddress), serverport);

			/*
			 * prepare socket output channel
			 */
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            
            /*
             * send syncronization request
             */

            out.println(request + ":" + localport);
            out.flush();

            /*
             * close connection
             */
            socket.close();

        } catch (Exception e){
            //e.printStackTrace();
            logger.info("Server: error ocured while sending request to localhost " + destinationPort);
        }
    }
    
}
