package ds.examples.sockets.ex_9;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;


public class Peer {
	String host;
	int port;
	Logger logger;


	public Peer(String hostname,String port) {
		host = hostname;
		this.port = Integer.parseInt(port);
		logger = Logger.getLogger("logfile");
		try {
			FileHandler handler = new FileHandler("./" + hostname + "_peer_" + port +".log", false);
			logger.addHandler(handler);
			SimpleFormatter formatter = new SimpleFormatter();
			handler.setFormatter(formatter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		Peer peer = new Peer(args[0],args[1]);
		System.out.printf("new peer @ host=%s\n", args[0]);

		//Start server thread
		Server server =new Server(args[0], Integer.parseInt(args[1]), peer.logger); 

		new Thread(server).start();

		//Start SyncronizedRequest
		SyncronizedRequest syncronizedRequest = new SyncronizedRequest(peer.host,peer.port, Integer.parseInt(args[2]), peer.logger);
		new Thread(syncronizedRequest).start();

		


	}
}


/**
 * Deals with Syncronization requests 
 */
class Server implements Runnable {
	String host;
	int port;
	ServerSocket server;
	Logger logger;

	public Server(String host, int port, Logger logger) throws Exception {
		this.host = host;
		this.port = port;
		this.logger = logger;
		server = new ServerSocket(port, 1, InetAddress.getByName(host));
	}

	@Override
	public void run() {
		try {
			logger.info("server: endpoint running at port " + port + " ...");
			while (true) {
				try {
					//Waits for syncronization request
					Socket client = server.accept();
					String clientAddress = client.getInetAddress().getHostAddress();
					
					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String request = in.readLine(); // Recebe o pedido 

					if (isSyncronizationRequest(request)){
						int senderPort = getOriginPort(request);

						logger.info("Server received SYNCRONIZATION request from " + clientAddress + " @" +senderPort);
					}
					
					

					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Checks if a string is a Syncronization Request
	 * (basicaly checks if request contains the string "SYNC-DATA")
	 * @param request
	 * @return
	 */
	private Boolean isSyncronizationRequest(String request){
		return request.contains("SYNC-DATA");
	}

	/**
	 * 
	 * @param request  SYNC-DATA:port_of_sender
	 * @return port that originated this request
	 */
	private int getOriginPort(String request){
		Scanner sc = new Scanner(request).useDelimiter(":");
		sc.next();
		String ans = sc.next();
		
		return Integer.parseInt(ans);

	}
}