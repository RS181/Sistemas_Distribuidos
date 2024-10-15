package ds.examples.sockets.ex_8;

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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * exemplo:
 * java Peer localhost porta_deste_peer porta_peer_que_vamos_conectar 'token'
 * 
 * No caso do argumento 'token' se ele for igual a:
 * -> "" , quer dizer que esse Peer nao comecou com o token
 * -> "Token", quer dizer que esse Peer comecou com o token
 */

public class Peer {
	String host;
	Logger logger;
	public static Boolean printLogMsg = true;

	public Peer(String hostname) {
		host = hostname;
		logger = Logger.getLogger("logfile");
		try {
			FileHandler handler = new FileHandler("./" + hostname + "_peer.log", true);
			logger.addHandler(handler);
			SimpleFormatter formatter = new SimpleFormatter();
			handler.setFormatter(formatter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 *             args[0] -> hostname do Peer
	 *             args[1] -> Porta do Peer atual
	 *             args[2] -> Porta do peer a que vamos conectar
	 *             args[3] -> token de iniciacao ("" -> Peer nao comeca com token,
	 *             "Token" -> Peer comeca com token)
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Peer peer = new Peer(args[0]);
		System.out.printf("new peer @ host=%s\n", args[0]);

		new Thread(new Server(args[0], Integer.parseInt(args[1]), peer.logger)).start();

		String token = args[3];
		new Thread(new Client(args[0], args[1], peer.logger, args[2], token)).start();
	}
}

class Server implements Runnable {
	String host;
	int port;
	ServerSocket server;
	Logger logger;
	Boolean printServerConectionLog = false; // colocar a true para disponbilizar Log

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
					Socket client = server.accept();
					String clientAddress = client.getInetAddress().getHostAddress();

					if (printServerConectionLog)
						logger.info("server: new connection from " + clientAddress);

					new Thread(new Connection(clientAddress, client, logger)).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/**
 * Basically receives a message from a client (token), if that
 * message is equal to :
 * -> "" (Connection send's "Token" to client)
 * -> "Token" (Connection send's "" to client)
 * 
 */
class Connection implements Runnable {
	String clientAddress;
	Socket clientSocket;
	Logger logger;

	public Connection(String clientAddress, Socket clientSocket, Logger logger) {
		this.clientAddress = clientAddress;
		this.clientSocket = clientSocket;
		this.logger = logger;
	}

	@Override
	public void run() {
		/*
		 * prepare socket I/O channels
		 */
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

			/**
			 * checks if user sent "Token" meaning he has the token
			 */
			String token = in.readLine();
			//logger.info("Server: received this cmd = [" + token + "]");

			/*
			 * send token if only if client did not
			 * send "Token"
			 */
			if (token.equals("Token"))
				out.println("");
			else
				out.println("Token");
			out.flush();

			/*
			 * close connection
			 */
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/**
 * Peer client que basicamente realiza a conexao
 * com o outro Peer server e "recebe" um token
 * 
 * No inicio, esse token deve ser injetado num dos peers
 * (pelo que eu percebi, temos de criar um programa a parte
 * que injeta o token nos peers)
 */

class Client implements Runnable {
	String host;
	String port;
	String otherPeerPort;
	String otherPeerHost = "localhost";
	String token; // "" -> no token; "token" ->client has token
	Logger logger;
	Scanner scanner;
	Boolean errorFlag = false;

	// Basicamente permite atualizar automaticamente etse valor em
	// outras threads do Client
	AtomicBoolean firstConnection = new AtomicBoolean(true);

	public Client(String host, String port, Logger logger, String otherPeerPort, String token) throws Exception {
		this.host = host;
		this.port = port;
		this.logger = logger;
		this.otherPeerPort = otherPeerPort;
		this.scanner = new Scanner(System.in);
		this.token = token;
	}

	@Override
	public void run() {
		Object lock = new Object(); // for debugging

		try {
			logger.info("client: endpoint running ...\n");
			int numberOfIterations = 5;
			while (true && numberOfIterations != 0) {

				synchronized (lock) {
					lock.wait(8000);	// wait 8 secconds for each token switch
					logger.info(System.lineSeparator());
					try {

						// Verificar se e a primeira conexao e se o Peer comecou com o token
						if (firstConnection.compareAndSet(true, false) && token.equals("Token")) {
							logger.info("First connection of client with TOKEN at @" + port);
						}

						/*
						 * make connection
						 */
						Socket socket = new Socket(InetAddress.getByName(otherPeerHost),
								Integer.parseInt(otherPeerPort));

						/*
						 * prepare socket I/O channels
						 */

						PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

						/*
						 * send command
						 */
						out.println(token);
						out.flush();

						/*
						 * receive result
						 */
						token = in.readLine();

						/*
						 * close connection
						 */
						socket.close();

						if (token.equals("Token"))
							logger.info("client at @" + port + " IS holding the " + token);
						else
							logger.info("client at @" + port + " NOT holding the token ");

						errorFlag = false;

						numberOfIterations--;

					} catch (Exception e) {
						if (!errorFlag) {
							errorFlag = true;
							System.out.println("Peer @" + otherPeerHost + " " + otherPeerPort + " is OFF-LINE");
						}
						// e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * Method that decides ,based on a coin toss if a
	 * Peer client is going to send a command to the
	 * server
	 * 
	 * @return true
	 */
	static Boolean interactWithServer() {
		return coinToss() == 1;
	}

	/**
	 * 
	 * @return 0 or 1 (simulating a coin toss)
	 */
	static int coinToss() {
		return (int) Math.round(Math.random());
	}
}
