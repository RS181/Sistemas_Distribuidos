package ds.assign.entropy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Class that contains information of ther neighbours Peer's
 * of a Peer
 */
public class PeerConnection {

    
    String host;
    int port;

    PeerConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // List of direct neighbours
    // Key-> port
    // value -> hostname
    private Map<Integer, String> neighbours = new HashMap<>();

    // key -> hostname-hostport
    // value -> timestamp in UTC seconds (that the peer last registered itself)
    private Map<String, Long> neighbourTimestamp = new HashMap<>();

    public Map<Integer, String> getNeighbours() {
        return neighbours;
    }

    public Map<String, Long> getNeighbourTimestampMap(){
        return neighbourTimestamp;
    }

    /**
	 * 
	 * @param map
	 * @param port
	 * @param address
	 * @return the map with updated timestamp on a certain peer
	 */

	public  Map<String,Long> updateTimestampMap(Map <String,Long> map,int port, String address){
        map.put(address + "-" + port, Instant.now().getEpochSecond());
        return map;
    }


    public void addNeighbour(int port, String address) {
        neighbours.put(port, address);
        neighbourTimestamp.put(address + "-" + port, Instant.now().getEpochSecond());
    }

    /**
     * 
     * @return a string in the format: "hostname:port" of
     *         available direct neighbours  (excluding itself)
     */
    public String chooseRandomNeighbour() {
        List<Integer> ports = new ArrayList<>(neighbours.keySet());

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
