package ds.examples.sockets.ex_9_extra;

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

    PeerConnection(){

    }

    // Key-> port 
    // value -> hostname 
    private Map <Integer,String> neighbours = new HashMap<>();



    public Map<Integer, String> getNeighbours() {
        return neighbours;
    }



    public void addNeighbour(int port,String address) {
        neighbours.put(port, address);
    }    

    /**
     * 
     * @return a string in the format: "hostname:port" of 
     * available neighbours
     */
    public String chooseRandomNeighbour(){
        List <Integer> ports = new ArrayList<>(neighbours.keySet());


        Random rand = new Random();

        int randomInt = rand.nextInt(ports.size());
        int randomPort = ports.get(randomInt);
        //System.out.println(randomInt);

        String address = neighbours.get(randomPort);
        return address + ":" + randomPort;
        
    }

    // public static void main(String[] args) {
    //     PeerConnection p = new PeerConnection();
    //     p.addNeighbour(1, "localhost");
    //     p.addNeighbour(2, "localhost");
    //     p.addNeighbour(3, "localhost");




    //     System.out.println(p.chooseRandomNeighbour());

    // }

}


