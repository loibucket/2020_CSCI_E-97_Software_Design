package cscie97.smartcity.helper;

import cscie97.smartcity.model.IoTDevice;

import java.util.*;

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
     * Calculate distance between two points in latitude and longitude taking
     * Uses Haversine method as its base.
     * https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
     *
     * @return distance between 2 locations
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
     * @param a    command passed in as a list of words
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
     * Helper: get attribute from command
     *
     * @param command command passed in as a string
     * @param attr    attribute name
     * @return attribute value
     */
    public static String findAttr(String command, String attr) {
        List<String> a = Arrays.asList(command.split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
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
     * Helper, removes extra quotes from inputs
     *
     * @param s passed in string
     * @return cleaned up string without the extra quotes
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

    /**
     * Helper, get list of bots, sorted by nearest to device of interest
     *
     * @param device    the device of interest
     * @param deviceMap all the city devices
     * @return list of bots sorted by distance
     */
    public static List<BotDist> getBotsByDist(IoTDevice device, Map<String, IoTDevice> deviceMap) {
        List<BotDist> botList = new ArrayList<>();
        for (String botId : deviceMap.keySet()) {
            IoTDevice b = deviceMap.get(botId);
            if (b.getClass().getName().equals("cscie97.smartcity.model.Robot")) {
                botList.add(new BotDist(botId, Tool.distance(device.getLocation(), b.getLocation())));
            }
        }
        Collections.sort(botList);
        return botList;
    }

    /**
     * Helper, get list of bots, sorted by nearest to LOCATION of interest
     *
     * @param targetLocation the LOCATION of interest
     * @param deviceMap      all the city devices
     * @return list of bots sorted by distance
     */
    public static List<BotDist> getBotsByDist(Float[] targetLocation, Map<String, IoTDevice> deviceMap) {
        List<BotDist> botList = new ArrayList<>();
        for (String botId : deviceMap.keySet()) {
            IoTDevice b = deviceMap.get(botId);
            if (b.getClass().getName().equals("cscie97.smartcity.model.Robot")) {
                botList.add(new BotDist(botId, Tool.distance(targetLocation, b.getLocation())));
            }
        }
        Collections.sort(botList);
        return botList;
    }

    /**
     * Helper, report out the device details
     *
     * @param d the device or person
     */
    public static void report(Object d) {
        System.out.println(d);
        System.out.println(" "); // line break
    }
}
