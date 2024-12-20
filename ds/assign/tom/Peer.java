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


public class Peer {
	String host;
    String port;
	Logger logger;

    //List of neighbour peer's
    List<PeerConnection> neighbours;


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
     * @param args
     * args[0]   -> this Peer hostname
     * args[1]   -> this Peer port 
     * 
     * i > 1
     * args[i]   -> neighour Peer hostname
     * args[i+1] -> neighour Peer port
     * @throws Exception
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
		return Integer.compare(num1, num2);
	};

	PriorityQueue <String> queue = new PriorityQueue<>(comparator); 
	

	public Server(String host, int port,MultiCast multiCast ,Logger logger) throws Exception {
		this.host = host;
		this.port = port;
		this.logger = logger;
		this.multiCast = multiCast;
		// Increased backlog to 50 to add length to the queue of incoming connections.
		// I think 6 would be the minimum, because we have 6 peer's but i put a bigger number
		// to guarante that we don't have problems
		server = new ServerSocket(port, 50, InetAddress.getByName(host));
	}

	@Override
	public void run() {
		try {
			logger.info("server: endpoint running at port " + port + " ...");
			while (true) {
				try {
					Socket client = server.accept();
					String clientAddress = client.getInetAddress().getHostAddress();
					logger.info("server: new connection from " + clientAddress);

					/*
		 			* prepare socket I/O channels
		 			*/
					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					
					String request = in.readLine();

					synchronized(queue){

						//adjust local Lamport clock
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
							// else
							// logger.warning("ACK PARA MIM PROPRIO");
						}
						
						client.close();

						// add message to priority queue
						queue.add(request);
					}

					
					//NOTA:Imprimir diretamente uma PriorityQueue em Java usando System.out.println(priorityQueue) não garante que os elementos sejam exibidos em ordem 
					//logger.info("LOCAL QUEUE = " + queue);


				} catch (Exception e) {
					logger.warning("server: Error ocurred in Peer Server");
					//e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 
	 * @param msg
	 * @return the timestamp associated with a message (Ack or regular Msg)
	 */
	int getMessageTimestamp(String msg){
		if(msg.contains("ACK"))
			return Integer.parseInt(msg.split(":")[2]);
		else 
			return Integer.parseInt(msg.split(":")[1]);

	}



	// Method to extract the first integer  from the string.
	private static int extractFirstInteger(String str) {
		String[] parts = str.split(":");
		if (parts[0].equals("ACK"))
			return Integer.parseInt(parts[2]);
		else
			return Integer.parseInt(parts[1]);
	}
}