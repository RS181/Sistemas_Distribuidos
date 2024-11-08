package ds.examples.sockets.ex_9_extra;
import java.util.Random;
import java.util.logging.Logger;


/**
 * This thread (class) is responsible for adding
 * number to the Peer's local set data , using a 
 * Poisson process with a frequency of 4 numbers
 * per minute.
 */
public class NumberGenerator implements Runnable {


    private final String host;
    private final int localPort;
    private final Logger logger;
    private final Server server;
    private  PoissonProcess poissonProcess = null;

    public NumberGenerator (String host,int localPort, Logger logger,Server server){
        this.host = host;
        this.localPort = localPort;
        this.logger = logger;
        this.server = server;

        //Intialize PoissonProcess
        Random rng = new Random();
        double lambda = 4.0;   // 4 events per minute
        poissonProcess = new PoissonProcess(lambda, rng);
    }

    @Override
    public void run() {
        while (true) {
            double intervalTime = poissonProcess.timeForNextEvent() * 1000 * 60; // Converting to milliseconds

            try {
                Thread.sleep((long)intervalTime);
                Integer number = generateRandomNumber();
                //Use the synchronized modifier to prevent race conditions between threads.
                //Notice that we passed a parameter number to the synchronized block. 
                //This is the monitor object. The code inside the block gets synchronized 
                //on the monitor object. Simply put, only one thread 
                //per monitor object can execute inside that code block (to avoid adding the same request
                //to the queue of the peer's server)
                synchronized(number){
                    server.addNumberToData(number);
                }



            }catch (Exception e){
                e.printStackTrace(); 
            }

        }
    }

    /*
     * Generates a random number between 0 and 9999
     */
    private int generateRandomNumber(){
        return (int)(Math.random() * 10000);
    }

    
}
