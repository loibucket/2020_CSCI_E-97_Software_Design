package cscie97.smartcity.helper;

/**
 * The BotDist stores bot_id and distance from a given point
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-17
 */
public class BotDist implements Comparable<BotDist> {
    private final String bot;    // first field of a Pair
    private final Float dist;    // second field of a Pair

    /**
     * Constructor
     *
     * @param bot  the robot
     * @param dist the robot's distance from location
     */
    public BotDist(String bot, Float dist) {
        this.bot = bot;
        this.dist = dist;
    }

    /**
     * get reference to the bot
     *
     * @return reference to the bot
     */
    public String getBot() {
        return this.bot;
    }

    /**
     * get the distance
     *
     * @return distance
     */
    public Float getDist() {
        return this.dist;
    }

    /**
     * To string
     *
     * @return string representation of the class
     */
    @Override
    public java.lang.String toString() {
        return "BotDist{" +
                "bot=" + bot +
                ", dist=" + dist +
                '}';
    }

    /**
     * Compare to, specifies the sorting method for the class
     * Items are sorted by distance, smallest to largest
     *
     * @param b a BotDist object
     * @return a positive(b is closer) or negative(b is farther) integer or zero(b is same dist)
     */
    @Override
    public int compareTo(BotDist b) {
        return Math.round((float) getDist() * 1000 - (float) b.getDist() * 1000);
    }
}