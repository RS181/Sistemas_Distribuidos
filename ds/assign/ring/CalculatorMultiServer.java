package ds.assign.ring;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Arrays;

/**
 *Esta classe e parecida com a que esta 
 no pacote calculator, mas o metodo listen()
 e feito de forma diferente usando Thread.


 explicacao thread :https://www.w3schools.com/java/java_threads.asp
 */
public class CalculatorMultiServer {
	private ServerSocket server;
	private int serverPort = 44444;	//fixed serverport

	public CalculatorMultiServer(String ipAddress) throws Exception {
		this.server = new ServerSocket(serverPort, 1, InetAddress.getByName(ipAddress));
	}

	private void listen() throws Exception {
		final long TIMEOUT_MILLIS = 5 * 60 * 1000; // Time limit to wait (5 minutes) (I decided that 5 minutes was suficient)
		long startTime = System.currentTimeMillis();

		while (true) {

			try {
				// Waits for connection (with time limit) 
				server.setSoTimeout((int) (TIMEOUT_MILLIS - (System.currentTimeMillis() - startTime)));
				Socket client = this.server.accept();
				String clientAddress = client.getInetAddress().getHostAddress();
				System.out.printf("\r\nnew connection from %s\n", clientAddress);
				new Thread(new ConnectionHandler(clientAddress, client)).start();
			}
			catch(Exception e){
				System.out.println("Timeout: Calculator server did not receive request in given time limit.\nCalculator server will Shutdown ....");
				Thread.sleep(5000);
				System.exit(0); // Encerrar o programa ou tomar outra ação
			}
		}
	}

	public InetAddress getSocketAddress() {
		return this.server.getInetAddress();
	}

	public int getPort() {
		return serverPort;
	}

	public static void main(String[] args) throws Exception {
		CalculatorMultiServer app = new CalculatorMultiServer(args[0]);
		System.out.printf("\r\nrunning Caculator server: host=%s @ port=%d\n",
				app.getSocketAddress().getHostAddress(), app.getPort());
		app.listen();
	}
}

// https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html
class ConnectionHandler implements Runnable {
	String clientAddress;
	Socket clientSocket;

	public ConnectionHandler(String clientAddress, Socket clientSocket) {
		this.clientAddress = clientAddress;
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		/*
		 * prepare socket I/O channels
		 */
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

			while (true) {
				/*
				 * receive command
				 */
				String command;
				if ((command = in.readLine()) == null)
					break;
				else {
					System.out.printf("Message [%s] from %s @%s\n", getCommand(command),clientAddress,getPeerPortFromComand(command));
				}
				/*
				* process command
				*/

				String result = process(command);
			
				/*
				 * send result
				 */
				//out.println(String.valueOf(result));
				out.println(result);
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param command = op:x:y:port_of_connected_peer
	 * @return command in a single string 
	 */
	private String getCommand(String command) {
		Scanner sc = new Scanner(command).useDelimiter(":");

		String ans = sc.next() + " " + sc.next() + " " + sc.next();

		return ans;
	}

	/**
	 * 
	 * @param command = op:x:y:port_of_connected_peer
	 * @return port_of_connected_peer
	 */
	private String getPeerPortFromComand(String command) {
		Scanner sc = new Scanner(command).useDelimiter(":");
		
		//Consumes usele
		sc.next();
		sc.next();
		sc.next();


		String port = sc.next();
		return port;
	}

	/**
	 * 
	 * @param command
	 * @return
	 */
	private String process(String command){
		String result = "nothing";
		Scanner sc = new Scanner(command).useDelimiter(":");
		String op = sc.next();

		if (isArithmetic(op)) {
			double x = Double.parseDouble(sc.next());
			double y = Double.parseDouble(sc.next());

			switch (op) {
				case "add":
					result = String.valueOf(x + y);
					break;
				case "sub":
					result = String.valueOf(x - y);
					break;
				case "mul":
					result = String.valueOf(x * y);
					break;
				case "div":
					result = String.valueOf(x / y);
					break;
			}
		}
		else {
			String str = sc.next();
			String str2 = "";
			switch (op) {
				case "length":
					result = String.valueOf(str.length());
					break;
				case "equal":
					str2 = sc.next();
					result = String.valueOf(str.equals(str2));
					break;
				case "cat":	//Supondo que a operacao cat e a concatencao
					str2 = sc.next();
					result = str + str2;
					break;
				case "break":
					str2 = sc.next();
					result = Arrays.toString(str.split(str2));
					break;
				default:
					break;
			}
		}
		
		return result;
	}


	private Boolean isArithmetic(String operation){
		return operation.equals("add") || operation.equals("sub") || operation.equals("mul") || operation.equals("div");
	}

}
