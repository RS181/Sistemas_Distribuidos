package ds.assign.entropy;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
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
			FileHandler handler = new FileHandler("./ds/assign/entropy/logs/" + host + "_peer_" + port + ".log", false);
			logger.addHandler(handler);
			SimpleFormatter formatter = new SimpleFormatter();
			handler.setFormatter(formatter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * args[0] -> Address of current peer
	 * args[1] -> Port of local peer
	 * args[i] -> Address of neighbour peer
	 * args[i+1] -> Port of neighbour peer
	 * e.g java Peer localhost 22222 localhost 33333 localhost 44444 localhost 55555
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Peer peer = new Peer(args[0], args[1]);
		System.out.printf("new peer host=%s @ %s\n", args[0], args[1]);

		// Create PeerConection
		PeerConnection neighbourInfo = new PeerConnection(peer.host, peer.port);
		for (int i = 0; i < args.length; i += 2) {
			peer.logger.info("new neighbour " + args[i] + " @" + args[i + 1]);
			neighbourInfo.addNeighbour(Integer.parseInt(args[i + 1]), args[i]);
		}

		// Start server thread
		Server server = new Server(args[0], Integer.parseInt(args[1]), neighbourInfo, peer.logger);
		new Thread(server).start();

		// Start SyncronizedRequest thread
		SyncronizedRequest syncronizedRequest = new SyncronizedRequest(peer.host, peer.port, neighbourInfo,
				peer.logger);
		new Thread(syncronizedRequest).start();

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

	// key -> hostname:hostport
	// value -> timestamp in UTC seconds (that the peer last registered itself)
	Map<String, Long> data;

	// Atributes of Peer we are going to connect (given by PeerConection)
	PeerConnection neighbourInfo;
	String nextHost;
	int nextPort;

	public Server(String host, int port, PeerConnection neighbourInfo, Logger logger) throws Exception {
		this.host = host;
		this.port = port;
		this.neighbourInfo = neighbourInfo;
		this.logger = logger;
		server = new ServerSocket(port, 1, InetAddress.getByName(host));

		data = neighbourInfo.getNeighbourTimestampMap();
		System.out.println(data.toString());
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

					// Syncronized on data
					synchronized (data) {

						// logger.info("Server: "+host+" @" + port + " received = "+ request);
						// logger.info("Origin host= " + getOriginHost(request) + " @" +
						// getOriginPort(request));

						// If this peer is the one that received syncronization request
						if (isSyncronizationRequest(request)) {
							int senderPort = getOriginPort(request);

							logger.info(
									"Received SYNC request from = [" + getOriginHost(request) + ",@"
											+ senderPort + "]");

							// Updates the Info of peer that we are going to send data set
							// for syncronization
							nextHost = getOriginHost(request);
							nextPort = getOriginPort(request);

							// update timestamp of itself (each Peer naturaly has the most recent timestamp
							// of itself)
							data = neighbourInfo.updateTimestampMap(data, port, host);

							// Send the local Set to neigbour peer who asked for syncronization
							sendData(data);

						}
						// If this peer is the one receiving the syncronization response
						// that includes the sender's Set
						else if (isMessageForThisPeer(request)) {

							// logger.info("DEBUG = " + request);

							if (!request.contains("{}")) {
								Map<String, Long> resultMap = parseMapFromString(request);
								;
								// update timestamp of itself (each Peer naturaly has the most recent timestamp
								// of itself)
								data = neighbourInfo.updateTimestampMap(data, port, host);

								if (!resultMap.equals(data)) {
									logger.info("Received SYNC response from = [" + getOriginHost(request) + ",@"
											+ getOriginPort(request) + "]");

									// Merges current map and received map (to get the most recent timestamps)
									data = mergeMapWithMaxValue(resultMap, data);

									// Updates nextPort and nexthost of receving peer, to
									// send its set to sender peer
									nextHost = getOriginHost(request);
									nextPort = getOriginPort(request);

									// logger.info("DEBUG nextHost = " + nextHost);
									// logger.info("DEBUG nextPOrt = " + nextPort);

									logger.info("Server: @" + port + " local map after MERGE : " + data);

									// To achieve the same Set after a syncronization betwen peer's
									// the receiving peer must send it's set to sender peer
									sendData(data);

								}
							}
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

	private void sendData(Map aMap) {
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
			 * 
			 * data:this_peer_port:this_peer_host:receiving_peer_port:receiving_peer_host
			 */
			out.print(aMap + ":" + port + ":" + host + ":" + nextPort + ":" + nextHost);
			out.flush();
			/*
			 * close connection
			 */
			socket.close();

		} catch (Exception e) {
			logger.warning("Server: error ocurred while trying to send info to Peer " + nextHost + " @" + nextPort);
			//e.printStackTrace();
		}


	}

	private Map<String, Long> parseMapFromString(String input) {
		Scanner sc = new Scanner(input).useDelimiter(":");

		String set = sc.next();

		// Remove "{" e "}"
		String content = set.substring(1, set.length() - 1).trim();

		Map<String, Long> result = new HashMap<>();

		// Dividir a string pelos pares (separador é a vírgula)
		String[] pairs = content.split(", ");

		for (String pair : pairs) {
			String[] keyValue = pair.split("="); // Dividir chave e valor
			String key = keyValue[0].trim();
			Long value = Long.parseLong(keyValue[1].trim());
			result.put(key, value);
		}

		return result;

	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return the result of merging map a and b
	 *  NOTES:
	 *  	-> We choose the peer wich has the highest timestamp, when doing 
	 * 		the merge,to ensure we have the most recent information)
	 * 
	 * 		-> Also we check if any entry in map 'a' or 'b' has peer timestamp
	 * 		that is outdated (in this cause we considered outdated a timestamp
	 * 		that difer's 300s or 5 min from the current timestamo) and remove it
	 * 		from map 
	 */
	public Map<String, Long> mergeMapWithMaxValue(Map<String, Long> a, Map<String, Long> b) {

		// Create a new map that is going to represent the merged map of 'a' and 'b'
		Map<String, Long> mergedMap = new HashMap<>();


		// For loop's that merge both maps (and check for outdated entries)
		
		for (Map.Entry<String, Long> entry : a.entrySet()) {

			if (Instant.now().getEpochSecond() - entry.getValue() < 300)
				mergedMap.put(entry.getKey(), entry.getValue());
			else
				logger.info(entry.getKey() + " is outdated in map 'a'. Removing...");
		}

		for (Map.Entry<String, Long> entry : b.entrySet()) {
			if (Instant.now().getEpochSecond() - entry.getValue() < 300)
				mergedMap.merge(entry.getKey(), entry.getValue(), Long::max);
			else 
				logger.info(entry.getKey() + " is outdated in map 'b'. Removing...");

		}

		return mergedMap;
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
	 * 
	 * @return
	 */
	private Boolean isMessageForThisPeer(String request) {
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
	 *  if (request is a syn request)
	 *  request = SYNC-DATA:port_of_sender:hostname_of_sender
	 *  if (request came from senddata())
	 *  request
	 *  data:sender_peer_port:sender_peer_host:receiving_peer_port:receiving_peer_host
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
	 *  if (request is a syn request)
	 *  request = SYNC-DATA:port_of_sender:hostname_of_sender
	 *  if (request came from senddata())
	 *  request
	 *  data:sender_peer_port:sender_peer_host:receiving_peer_port:receiving_peer_host
	 * @return hostname that originated this request
	 */
	private String getOriginHost(String request) {
		Scanner sc = new Scanner(request).useDelimiter(":");
		sc.next();
		sc.next();
		return sc.next();
	}

}