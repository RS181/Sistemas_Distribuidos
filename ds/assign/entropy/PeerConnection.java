package ds.assign.entropy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Represents the connection information for a peer in a distributed system.
 * Stores information about the peer's direct neighbors and their synchronization timestamps.
 * 
 * @see <a href="https://github.com/RS181/">Repository</a>
 * @author Rui Santos
 */
public class PeerConnection {

    String host;
    int port;

    // List of direct neighbours
    // Key-> port
    // value -> hostname
    private Map<Integer, String> neighbours = new HashMap<>();

    // key -> hostname-hostport
    // value -> timestamp in UTC seconds (that the peer last registered itself)
    private Map<String, Long> neighbourTimestamp = new HashMap<>();

    /**
     * Constructs a PeerConnection instance with the specified host and port.
     *
     * @param host the hostname of the peer
     * @param port the port number of the peer
    */
    PeerConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Returns the map of direct neighbors.
     *
     * @return a map where the key is the neighbor's port and the value is the neighbor's hostname
    */
    public Map<Integer, String> getNeighbours() {
        return neighbours;
    }

    /**
     * Returns the timestamp map of neighbors.
     *
     * @return a map where the key is "hostname-port" and the value is the last synchronization timestamp
    */
    public Map<String, Long> getNeighbourTimestampMap(){
        return neighbourTimestamp;
    }

    /**
     * Updates the timestamp of a specific peer in the timestamp map.
     *
     * @param map     the current timestamp map
     * @param port    the port of the peer to update
     * @param address the hostname of the peer to update
     * @return the updated timestamp map
    */
	public  Map<String,Long> updateTimestampMap(Map <String,Long> map,int port, String address){
        map.put(address + "-" + port, Instant.now().getEpochSecond());
        return map;
    }


    /**
     * Adds a neighbor to the neighbor list and updates its timestamp.
     *
     * @param port    the port of the neighbor to add
     * @param address the hostname of the neighbor to add
    */
    public void addNeighbour(int port, String address) {
        neighbours.put(port, address);
        neighbourTimestamp.put(address + "-" + port, Instant.now().getEpochSecond());
    }

    /**
     * Selects a random neighbor from the list of available neighbors, excluding the current peer itself.
     *
     * @return a string in the format "hostname:port" representing the randomly selected neighbor
     */
    public String chooseRandomNeighbour() {
        List<Integer> ports = new ArrayList<>(neighbours.keySet());

        //remove current peer
        for (int i = 0 ; i < ports.size() ; i++){
            if (ports.get(i) == port)
                ports.remove(i);
        }

        Random rand = new Random();

        int randomInt = rand.nextInt(ports.size());
        int randomPort = ports.get(randomInt);

        String address = neighbours.get(randomPort);
        return address + ":" + randomPort;

    }

}
