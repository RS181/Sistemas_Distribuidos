package ds.assign.tom;


import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.Comparator;


/**
 * Represents a peer in a basic chat application that implements 
 * totally-ordered multicast for message synchronization.
 *
 * <p>Each peer can send and receive messages while ensuring a consistent 
 * order of delivery across all participants. This is achieved using 
 * Lamport clocks and a priority queue for message processing.</p>
 * 
 * @see <a href="https://github.com/RS181/">Repository</a>
 * @author Rui Santos
 */
public class Peer {
	String host;
    String port;
	Logger logger;

    //List of neighbour peer's
    List<PeerConnection> neighbours;

	/**
     * Constructs a Peer instance with the specified parameters.
     *
     * @param host  the hostname of this peer
     * @param port  the port of this peer
     * @param args  command-line arguments specifying neighbor information
    */
	public Peer(String host,String port,String[] args) {
		this.host = host;
        this.port = port;

		
        logger = Logger.getLogger("logfile");
		try {
			FileHandler handler = new FileHandler("./ds/assign/tom/logs/" + host + "_peer_" + port + ".log", false);
			logger.addHandler(handler);
			SimpleFormatter formatter = new SimpleFormatter();
			handler.setFormatter(formatter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Initialize neighbour's  
		neighbours = new ArrayList<>();
		for (int i = 2; i< args.length ; i+=2 ){
			PeerConnection neigh = new PeerConnection(args[i], Integer.parseInt(args[i+1]));
			neighbours.add(neigh);
			//System.out.println(neigh);
			//logger.info("Peer: Neihbour -> " + neigh);
		}
	}

    /**
     * 
	 * The main entry point for the chat application. Initializes the peer, 
     * its server, multicast functionality, and message queue monitor.
     *
     * @param args
     * args[0]   -> this Peer hostname
     * args[1]   -> this Peer port 
     * 
     * i > 1
     * args[i]   -> neighour Peer hostname
     * args[i+1] -> neighour Peer port
     * @throws Exception if initialization fails
     */
	public static void main(String[] args) throws Exception {
		Peer peer = new Peer(args[0],args[1],args);
		System.out.printf("new peer @ host=%s\n", args[0]);
        
		MultiCast multiCast = new MultiCast(peer);
		new Thread(multiCast).start();


		Server server = new Server(args[0], Integer.parseInt(args[1]),multiCast, peer.logger);
		new Thread(server).start();

		QueueMonitor queueMonitor = new QueueMonitor(server.queue, args[0], Integer.parseInt(args[1]), peer.neighbours.size() + 1, peer.logger);
		new Thread(queueMonitor).start();
		
	}
}

/**
 * Represents a server within the peer, responsible for receiving and processing 
 * incoming messages in the chat application. Messages are synchronized using 
 * totally-ordered multicast.
 */

class Server implements Runnable {
	String host;
	int port;
	ServerSocket server;
	Logger logger;
	MultiCast multiCast;

	// Custom comparator to sort strings based on the first integer in the format.
	// In this case the first integer corresponds to LamportCLock
	Comparator<String> comparator = (s1, s2) -> {
		int num1 = extractFirstInteger(s1);
		int num2 = extractFirstInteger(s2);

		// Compares Lamport clock's
		if (num1 != num2) {
			return Integer.compare(num1, num2);
		}

		// Extract  PID (IP and  Port) of messages
		String pid1 = extractPid(s1);
		String pid2 = extractPid(s2);

		// Compare in lexicographic order
		return pid1.compareTo(pid2);
	};

	// Priority queue for managing received messages based on Lamport timestamps
	PriorityQueue <String> queue = new PriorityQueue<>(comparator); 
	
    /**
     * Constructs a Server instance for managing incoming messages.
     *
     * @param host       the hostname of the server
     * @param port       the port the server listens on
     * @param multiCast  the MultiCast instance for totally-ordered multicast
     * @param logger     the Logger for recording server activity
     * @throws Exception if the server fails to initialize
     */
	public Server(String host, int port,MultiCast multiCast ,Logger logger) throws Exception {
		this.host = host;
		this.port = port;
		this.logger = logger;
		this.multiCast = multiCast;
		// Increased backlog to 50 to add length to the queue of incoming connections.
		// I think 6 would be the minimum, because we have 6 peer's but i put a bigger number
		// to guarante that we don't have problems

		// WHEN THE 2ND ARGUMENT BELLOW IS VERY HIGH, IT WORK'S

		server = new ServerSocket(port, 10000, InetAddress.getByName(host));



		multiCast.setQueue(queue);
	}

	/**
     * Starts the server to listen for incoming connections, process messages, 
     * and update the local priority queue.
     */
	@Override
	public void run() {
		try {
			logger.info("server: endpoint running at port " + port + " ...");
			while (true) {
				try {
					Socket client = server.accept();
					String clientAddress = client.getInetAddress().getHostAddress();
					logger.info("server: new connection from " + clientAddress);

					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					String request = in.readLine();

					synchronized(queue){

						// Adjust local Lamport clock based on incoming message
						int ts = getMessageTimestamp(request);
						int Cj = Math.max(ts, multiCast.getLamportClock().getTime()) + 1;
						multiCast.getLamportClock().setTime(Cj);



						if (request.contains("ACK")){
							logger.info("[ACK] Server: received " + request);
						}
						else {
							logger.info("[MSG] Server received " + request);
							// To avoid responding to an ACK with a ACK 
							if (!request.split(":")[3].equals(String.valueOf(port))){
								synchronized(request){
									String word = request.split(":")[0];
									multiCast.sendAck(word);
								}
							}
						}
						
						client.close();

						// add message to priority queue
						queue.add(request);
					}
				} catch (Exception e) {
					logger.warning("server: Error ocurred in Peer Server");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    /**
     * Extracts the timestamp from a message (ACK or regular).
     *
     * @param msg the message string
     * @return the timestamp as an integer
     */
	int getMessageTimestamp(String msg){
		if(msg.contains("ACK"))
			return Integer.parseInt(msg.split(":")[2]);
		else 
			return Integer.parseInt(msg.split(":")[1]);

	}



	/**
     * Extracts the first integer (Lamport clock) from a message string.
     *
     * @param str the message string
     * @return the extracted integer
     */
	private static int extractFirstInteger(String msg) {
		String[] parts = msg.split(":");
		if (parts[0].equals("ACK"))
			return Integer.parseInt(parts[2]);
		else
			return Integer.parseInt(parts[1]);
	}


	/**
	 * Extracts PID in a given msg
	 * 
	 * 
	 * @param message
	 * @return the extracted PID
	 */
	private String extractPid(String msg) {
    	// Identifica se é um ACK ou mensagem direta e separa o PID
    	String[] parts = msg.split(":");
    	if (msg.startsWith("ACK")) {
    	    return parts[3] + ":" + parts[4]; 
    	} else {
    	    return parts[2] + ":" + parts[3]; 
    	}
	}
}