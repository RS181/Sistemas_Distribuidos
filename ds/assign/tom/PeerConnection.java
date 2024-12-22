package ds.assign.tom;


/**
 * Represents a connection to a peer in the chat system, storing the peer's
 * host (IP address or hostname) and port for establishing communication.
 *
 * <p>This class is used to define the connection details of other peers 
 * in a distributed system, enabling the establishment of network communication
 * channels between peers participating in the chat.</p>
 * 
 * 
 * @see <a href="https://github.com/RS181/">Repository</a>
 * @author Rui Santos
 */
public class PeerConnection {
    private String host;
    private int port;

    public PeerConnection (String host, int port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }
}
