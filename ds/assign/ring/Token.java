package ds.assign.ring;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Token class that basicaly injects, for the first time, a token in a Peer
 */
public class Token {

    static String token = "Token";

    private static void sendToken(String host, int port) {
        
        try {
            /*
            * Create comunication Socket 
            */
            Socket socket = new Socket(host,port);

            /*
             * Prepare socket I/O channel (in this case only output)
             */
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            /*
             * Send token 
             */
            out.println(token);
            out.flush();

            /*
            * Close connection
            */
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        } 


    }


    /**
     * 
     * @param args
     * args[0] -> Peer hostname
     * args[1] -> Peer port
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        sendToken(host,port);

        System.out.println("Token was sent successfuly");
    }


 
}
