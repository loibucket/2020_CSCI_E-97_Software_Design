package cscie97.smartcity;

import java.util.List;

/**
 * Tool
 * A set of helper methods used across multiple classes
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-17
 */
public class Tool {

    /**
     * https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
     * Calculate distance between two points in latitude and longitude taking
     * Uses Haversine method as its base.
     * @return
     */
    public static Float distance(Float[] locA, Float[] locB) {

        double lat1 = locA[0];
        double lat2 = locB[0];

        double lon1 = locA[1];
        double lon2 = locB[1];

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // km

        distance = Math.pow(distance, 2);

        return (float) Math.sqrt(distance);
    }

    /**
     * Helper: get attribute from command
     *
     * @param a    command
     * @param attr attribute name
     * @return attribute value
     */
    public static String findAttr(List<String> a, String attr) {
        int Idx = a.indexOf(attr);
        if (Idx == -1) {
            return null;
        } else if (a.size() > Idx + 1) {
            return clean(a.get(Idx + 1));
        } else {
            return null;
        }
    }

    /**
     * helper, removes extra quotes from inputs
     *
     * @param s
     * @return
     */
    public static String clean(String s) {
        //cleanup remove quotes if exists
        if (s.startsWith("\"")) {
            s = s.substring(1);
        }
        if (s.endsWith("\"")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

}
