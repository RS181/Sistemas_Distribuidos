package ds.examples.sockets.ex_5;
import java.util.Random;
import java.util.logging.Logger;
import java.net.Socket;
import java.net.InetAddress;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;



/**
 * A thread that generates a random request and sends it 
 * to a given Peer (basicaly acts as a client)
 */
public class RequestGenerator implements Runnable{

    private final String host;
    private final int destinationPort;
    private final Logger logger;
    private PoissonProcess poissonProcess = null;
    private String[] presentPeersPoort;


   
    public RequestGenerator (String host, int port, Logger logger){
        this.host = host;
        this.destinationPort = port;
        this.logger = logger;

        //Intialize PoissonProcess
        Random rng = new Random();
        double lambda = 5.0;    
        poissonProcess = new PoissonProcess(lambda, rng);
    }


    @Override
    public void run() {
        while (true) {
            double intervalTime = poissonProcess.timeForNextEvent() * 1000 * 60; // Converting to milliseconds

            try {
                Thread.sleep((long)intervalTime);
                String request = generateRandomRequest();
                sendRequestToServer(request,destinationPort);

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
    private void sendRequestToServer(String request, int serverport) {
        String serverAddress = "localhost";

        try{
            /*
             * make connection
             */
            Socket socket = new Socket(InetAddress.getByName(serverAddress), serverport);

			/*
			 * prepare socket I/O channels
			 */
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            
            /*
             * send command
             */

            out.println(request);
            out.flush();
        
            /*
			* receive result
			*/
			String result = in.readLine();
			System.out.printf("Result = %f\n", Double.parseDouble(result));


            /*
             * close connection
             */

            //socket.close();

        } catch (Exception e){
            //e.printStackTrace();
            logger.info("Server: error ocured while sending request to localhost " + destinationPort);
        }
    }


    /**
     * 
     * @return a String of a request in the format
     * "server_adress port operation x y"
     * server_adress = localhost
     * where x and y are double'sse
     * 
     */
    private String generateRandomRequest() {

        String[] operations = {"add" , "sub" , "mul" , "div"};

        Random random = new Random();
        String operation = operations[random.nextInt(operations.length)];

        double x = random.nextDouble() * 100;
        double y = random.nextDouble() * 100;

        return host + " " + destinationPort + " " +  operation + " " + x + " " + y;
    }
    


}
