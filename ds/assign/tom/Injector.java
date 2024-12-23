package ds.assign.tom;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Injector para inicializar todos os peers com um comando READY.
 * 
 * Uso:
 * java Injector host1:port1 host2:port2 ...
 */
public class Injector {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Error: incorrect format");
            System.exit(1);
        }

        List<String> peers = new ArrayList<>();
        for (String arg : args) {
            peers.add(arg);
        }

        // Send Ready command to all peer's
        for (String peer : peers) {
            String[] parts = peer.split(":");
            if (parts.length != 2) {
                System.err.println("Invalid format: " + peer + ". Use host:port.");
                continue;
            }

            String host = parts[0];
            int port;

            try {
                port = Integer.parseInt(parts[1]) + 1000;
            } catch (NumberFormatException e) {
                System.err.println("Porta inválida no peer: " + peer);
                continue;
            }

            try (Socket socket = new Socket(host, port);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                out.println("READY");
                System.out.println("Sent READY to " + peer);

            } catch (Exception e) {
                System.err.println("Failed to send READY to " + peer + ": " + e.getMessage());
            }
        }

        System.out.println("All peers have been notified with READY.");
    }
}
