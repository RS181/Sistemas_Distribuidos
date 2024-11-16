package ds.examples.sockets.ex_9_extra;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

public class Peer {
	String host;
	int port;
	Logger logger;

	public Peer(String hostname, String port) {
		host = hostname;
		this.port = Integer.parseInt(port);
		logger = Logger.getLogger("logfile");
		try {
			FileHandler handler = new FileHandler("./" + hostname + "_peer_" + port + ".log", false);
			logger.addHandler(handler);
			SimpleFormatter formatter = new SimpleFormatter();
			handler.setFormatter(formatter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * args[0]   -> Address of current peer
	 * args[1]   -> Port of local peer
	 * args[i] 	 -> Address of neighbour peer
	 * args[i+1] -> Port of neighbour peer
	 * e.g java Peer localhost 22222 localhost 33333 localhost 44444 localhost 55555
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Peer peer = new Peer(args[0], args[1]);
		System.out.printf("new peer host=%s @ %s\n", args[0],args[1]);

		//Create PeerConection 
		PeerConnection neighbourInfo = new PeerConnection();
		for (int i = 2 ; i < args.length;i+=2){
			peer.logger.info("new neighbour "+ args[i] + " @"+args[i+1]);
			neighbourInfo.addNeighbour(Integer.parseInt(args[i+1]), args[i]);
		}

		// Start server thread
		Server server = new Server(args[0], Integer.parseInt(args[1]),neighbourInfo, peer.logger);
		new Thread(server).start();



		// Start SyncronizedRequest thread
		SyncronizedRequest syncronizedRequest = 
		new SyncronizedRequest(peer.host, peer.port,neighbourInfo,peer.logger);
		new Thread(syncronizedRequest).start();

		//TODO - fazer alteracoes para aceitar multiplos Peer's
		// Start NumberGenerator thread
		NumberGenerator numberGenerator = new NumberGenerator(peer.host, peer.port, peer.logger, server);
		new Thread(numberGenerator).start();

	}
}

/**
 * Deals with Syncronization requests
 */
class Server implements Runnable {
	


	// Atributes of current peer
	String host;
	int port;
	ServerSocket server;
	Logger logger;
	Set<Integer> data = new HashSet<>(); // Local Set of data that we want to syncronize
	Set<Integer> dataBeforeMerge = new HashSet<>();
	
	// Atributes of Peer we are going to connect (given by PeerConection)
	PeerConnection neighbourInfo;
	String nextHost; 
	int nextPort; 

	public Server(String host, int port,PeerConnection neighbourInfo , Logger logger) throws Exception {
		this.host = host;
		this.port = port;
		this.neighbourInfo = neighbourInfo;
		this.logger = logger;
		server = new ServerSocket(port, 1, InetAddress.getByName(host));


	}

	public void addNumberToData(int n) {
		data.add(n);
	}

	@Override
	public void run() {
		try {
			logger.info("server: endpoint running at port " + port + " ...");
			while (true) {
				try {
					// Waits for syncronization request
					Socket client = server.accept();
					String clientAddress = client.getInetAddress().getHostAddress();

					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					String request = in.readLine(); // Recebe o pedido

					Boolean updateSenderPeer = false;

					synchronized (data) {

						// logger.info("Server: "+host+" @" + port + " received = "+ request);
						// logger.info("Origin host= " + getOriginHost(request) + " @" + getOriginPort(request));
						
						// If this peer is the one that received syncronization request
						if (isSyncronizationRequest(request)) {
							int senderPort = getOriginPort(request);

							logger.info(
									"Server: received SYNCRONIZATION request from " + clientAddress + " @"
											+ senderPort);

							// Updates the Info of peer that we are going to send data set
							// for syncronization
							nextHost = getOriginHost(request);
							nextPort = getOriginPort(request);
							
							//Send the local Set to neigbour peer who asked for syncronization
							sendData(data);
							
						} 
						// If this peer is the one receiving the syncronization response 
						// that includes the sender's Set 
						else if (isMessageForThisPeer(request)){  

							//logger.info("DEBUG = " + request);
							// Set<Integer> resultSet = parseSetFromString(request);
							// logger.info("Server: @" + port + " :" + resultSet.toString());

							if (!request.contains("[]")) {
								Set<Integer> resultSet = parseSetFromString(request);
								if (!resultSet.equals(data)){
									
									//saves data set before merge
									dataBeforeMerge = new HashSet<>(data);

									//Updates nextPort and nexthost of receving peer, to 
									// send its set to sender peer 
									nextHost = getOriginHost(request);
									nextPort = getOriginPort(request);


									data = mergeSet(resultSet, data);
									

									// Sort data set (it helps with visualization)
									List<Integer> sortedData = new ArrayList<>(data);
									Collections.sort(sortedData);
									logger.info("Server: @" + port + " local set after MERGE : " + sortedData);
									
									//Indicates that the receving peer has to sends it's
									// data set to sender peer (to complete syncronization)
									updateSenderPeer = true;
								}
							}
						}
					}

					// To achieve the same Set after a syncronization betwen peer's
					// the receiving peer must send it's set to sender peer
					if (updateSenderPeer){
						synchronized(data){
							//send data before merge to save bandwith
							sendData(dataBeforeMerge);
						}
						
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendData(Set aSet) {
		try {
			/*
			 * make connection
			 */
			Socket socket = new Socket(nextHost, nextPort);

			/*
			 * prepare socket output channel
			 */
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			/*
			 * Sends the local set (the set will be wrapped in to a String)
			 * also send's information of peer who sent it and the peer who will receive it 
			 
			   data:this_peer_port:this_peer_host:receiving_peer_port:receiving_peer_host
			 */
			out.print(aSet+":" + port + ":" + host + ":" + nextPort + ":" + nextHost);
			out.flush();
			/*
			 * close connection
			 */
			socket.close();

		} catch (Exception e) {
			logger.info("Server: error ocurred while trying to send info to Peer " + nextHost + " @" + nextPort);
			e.printStackTrace();
		}

		// sendEndSyncronization();

	}

	private Set<Integer> parseSetFromString(String input) {
		Scanner sc = new Scanner(input).useDelimiter(":");

		String set = sc.next();

		// Remove  "[" e "]"
		String content = set.substring(1, set.length() - 1).trim();
		return Arrays.stream(content.split("\\s*,\\s*"))
					 .map(Integer::parseInt)
					 .collect(Collectors.toSet());
	}


	// Method 1
	// To merge two sets
	// using DoubleBrace Initialisation
	public static <T> Set<T> mergeSet(Set<T> a, Set<T> b) {

		// Adding all elements of respective Sets
		// using addAll() method
		return new HashSet<T>() {
			{
				addAll(a);
				addAll(b);
			}
		};
	}


	/**
	 * Checks if a string is a Syncronization Request
	 * (basicaly checks if request contains the string "SYNC-DATA")
	 * 
	 * @param request
	 * @return
	 */
	private Boolean isSyncronizationRequest(String request) {
		return request.contains("SYNC-DATA");
	}
	/**
	 * Check if the message received is for this current peer
	 * (check if message contains the same @host and @port has 
	 * this peer)
	 * 
	 * request= 
	 * data:sender_peer_port:sender_peer_host:receiving_peer_port:receiving_peer_host
	 * @return
	 */
	private Boolean isMessageForThisPeer(String request){
		Scanner sc = new Scanner(request).useDelimiter(":");
		sc.next();
		sc.next();
		sc.next();

		int requestPort = Integer.parseInt(sc.next());
		String requestHost = sc.next();
		
		return requestPort == port && requestHost.equals(host);
	}

	/**
	 * 
	 * @param request 
	 * if (request is a syn request)
	 * 		request = SYNC-DATA:port_of_sender:hostname_of_sender
	 * if (request came from senddata())
	 * 		request data:sender_peer_port:sender_peer_host:receiving_peer_port:receiving_peer_host
	 * @return port that originated this request
	 */
	private int getOriginPort(String request) {
		Scanner sc = new Scanner(request).useDelimiter(":");
		sc.next();
		String ans = sc.next();

		return Integer.parseInt(ans);

	}

	/**
	 * 
	 * @param request 
	 * if (request is a syn request)
	 * 		request = SYNC-DATA:port_of_sender:hostname_of_sender
	 * if (request came from senddata())
	 * 		request data:sender_peer_port:sender_peer_host:receiving_peer_port:receiving_peer_host
	 * @return hostname that originated this request
	 */
	private String getOriginHost(String request){
		Scanner sc = new Scanner(request).useDelimiter(":");
		sc.next();
		sc.next();
		return sc.next();
	}


}