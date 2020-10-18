package cscie97.smartcity;

/**
 * The BotDist stores bot_id and distance from a given point
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-17
 */
public class BotDist implements Comparable<BotDist>{
    private final String bot;    // first field of a Pair
    private final Float dist;    // second field of a Pair

    // Constructs a new Pair with specified values
    public BotDist(String bot, Float dist) {
        this.bot = bot;;
        this.dist = dist;
    }

    public String getBot(){
        return this.bot;
    }

    public Float getDist(){
        return this.dist;
    }

    @Override
    public java.lang.String toString() {
        return "BotDist{" +
                "bot=" + bot +
                ", dist=" + dist +
                '}';
    }

    @Override
    public int compareTo(BotDist o) {
        return Math.round((float)getDist()*1000 - (float)o.getDist()*1000);
    }
}