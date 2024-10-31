package ds.examples.sockets.ex_9;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

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

	public static void main(String[] args) throws Exception {
		Peer peer = new Peer(args[0], args[1]);
		System.out.printf("new peer @ host=%s\n", args[0]);

		// Start server thread
		Server server = new Server(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), peer.logger);
		new Thread(server).start();

		// Start SyncronizedRequest thread
		SyncronizedRequest syncronizedRequest = new SyncronizedRequest(peer.host, peer.port, Integer.parseInt(args[2]),
				peer.logger);
		new Thread(syncronizedRequest).start();

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

	// Atributes of Peer we are going to connect
	String nextHost = "localhost"; // assuming we are using localhost
	int nextPort;

	public Server(String host, int port, int nextPort, Logger logger) throws Exception {
		this.host = host;
		this.port = port;
		this.nextPort = nextPort;
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

					synchronized (data) {

						if (isSyncronizationRequest(request)) {
							int senderPort = getOriginPort(request);

							logger.info(
									"Server: received SYNCRONIZATION request from " + clientAddress + " @"
											+ senderPort);

							// do the merge of sets (todo:Verificar se esta e a forma pretendida)
							sendData();

						} else { // Receive numbers and put them in data Set
							int n = Integer.parseInt(request);

							if (!data.contains(n)) {
								// logger.info("Server: Adding [" + n + "] to local data Set");
								addNumberToData(Integer.parseInt(request));
								logger.info("Server: local data = " + data);
							} 
							// else {
							// logger.info("Server: The number [" + n + "] already exists in local data Set");
							// }

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

	private void sendData() {
		List<Integer> sentNumbers = new ArrayList<>();
		try {

			for (int n : data) {
				/*
				 * make connection
				 */
				Socket socket = new Socket(nextHost, nextPort);

				/*
				 * prepare socket output channel
				 */
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				/*
				 * Send number n
				 */
				out.println(n);
				out.flush();
				/*
				 * close connection
				 */
				socket.close();
				

				sentNumbers.add(n); // for better log messages
			}

		} catch (Exception e) {
			logger.info("Server: error ocurred while triing to send info to Peer " + nextHost + " @" + nextPort);
			e.printStackTrace();
		}

		if (!sentNumbers.isEmpty())
			logger.info("Server: sent  " + sentNumbers + " to @" + nextPort);

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
	 * 
	 * @param request SYNC-DATA:port_of_sender
	 * @return port that originated this request
	 */
	private int getOriginPort(String request) {
		Scanner sc = new Scanner(request).useDelimiter(":");
		sc.next();
		String ans = sc.next();

		return Integer.parseInt(ans);

	}
}