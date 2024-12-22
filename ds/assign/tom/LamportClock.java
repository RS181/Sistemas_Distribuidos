package ds.assign.tom;

/**
 * This class implements a Lamport clock for event ordering in distributed systems.
 * The Lamport clock is used to ensure the ordering of events between processes in a distributed system.
 * The class allows incrementing the time and accessing the current value of the clock.
 *
 * <p>The Lamport clock is used to ensure consistency of event order, especially when combined
 * with the totally-ordered multicast algorithm. Each time a message is sent, the clock's value is incremented
 * to guarantee that events are ordered  between peers.</p>
 * 
 * @see <a href="https://github.com/RS181/">Repository</a>
 * @author Rui Santos
 */
public class LamportClock {

    private int time = 0;

    public LamportClock(int time){
        this.time = time;
    }


    public int getTime() {
        return time;
    }



    public void setTime(int time) {
        this.time = time;
    }


    public void increment(){
        this.time++;
    }

}
