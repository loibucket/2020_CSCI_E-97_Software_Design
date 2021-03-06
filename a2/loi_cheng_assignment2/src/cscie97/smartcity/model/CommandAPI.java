package cscie97.smartcity.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The CommandAPI processes input commands from user and processes them through various methods
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class CommandAPI {

    private final Map<String, City> cityMap;
    private final Registry registry;

    /**
     * Constructor
     */
    public CommandAPI() {
        this.cityMap = new HashMap<>();
        this.registry = new Registry("peoples_01001");
    }

    /**
     * process a line of command
     *
     * @param authToken  authorization token
     * @param command    command
     * @param lineNumber line number if given
     */
    public void processCommand(String authToken, String command, int lineNumber) {

        if (authToken == null) {
            System.out.println("authorization token not provided!");
            return;
        }

        System.out.println(command);

        // replace special quotes to normal
        command = command.replace('“', '"');
        command = command.replace('”', '"');

        // split string by whitespace, except when between quotes
        // stackoverflow.com/questions/18893390/splitting-on-comma-outside-quotes
        List<String> a = Arrays.asList(command.split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));

        // do different action with commands
        String action = a.get(0);
        try {
            // create define update show, direct to helper functions
            switch (action) {
                case "create" -> create(a);
                case "define" -> define(a);
                case "show" -> show(a);
                case "update" -> update(a);
                default -> throw new ServiceException("command", "command not recognized");
            }
        } catch (ServiceException e) {
            System.out.println(new CommandException(command, e.action, e.reason, lineNumber).toString());
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            System.out.println(new CommandException(command, null, "too few arguments!", lineNumber).toString());
        } catch (java.lang.NullPointerException e){
            System.out.println(new CommandException(command, null, "not found!", lineNumber).toString());
        } catch (Exception e) {
            System.out.println(new CommandException(command, null, e.toString(), lineNumber).toString());
        }
        System.out.println("-end-"); //end command
        System.out.println(" "); //line break
    }

    /**
     * process a set of commands from file
     *
     * @param authToken   authorization token
     * @param commandFile command file name
     */
    public void processCommandFile(String authToken, String commandFile) {

        if (!authToken.equals("placeholder")) {
            System.out.println("authentication error");
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(commandFile));
            AtomicInteger lineNumber = new AtomicInteger(1);
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (line.charAt(0) != "#".charAt(0)) {
                        processCommand(authToken, line, lineNumber.get());
                    }
                    lineNumber.getAndIncrement();
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Helper: handle create commands
     * Slight variations in commands are accepted
     *
     * @param a the command
     * @throws ServiceException if cannot process command
     */
    private void create(List<String> a) throws ServiceException {
        switch (a.get(1)) {
            case "sensor-event", "sensor-output" -> {

                // Expected Commands
                //   0          1         2                      3                   4                         5       6        7        8
                // create sensor-event <city_id>[:<device_id>] [type] (microphone|camera|thermometer|co2meter) value <string> [subject <person_id>]
                //   0          1         2                      3      4        5      6
                // create sensor-output <city_id>[:<device_id>] [type] (speaker) value <string>
                //   0          1         2                         3       4      5         6
                // create sensor-output <city_id>[:<device_id>] (speaker) value <string> [<person_id>]
                //   0          1         2                         3       4      5
                // create sensor-output <city_id>[:<device_id>] (speaker) value <string>

                // check if 'type' is explicit in command
                // or else find type in the command
                String type = findAttr(a, "type");
                if (type == null) {
                    type = a.get(3);
                }

                // 'value' is always explicit in command
                String value = findAttr(a, "value");

                // check if 'subject is explicit in command
                // or else find subject in command
                String subject = findAttr(a, "subject");
                if (subject == null) {
                    int lastIdx = a.size() - 1;
                    int valuIdx = a.indexOf("value");
                    if (lastIdx - valuIdx > 1) { // more than one item after 'value'
                        subject = a.get(a.size() - 1); // set last item in command as the subject
                    }
                }

                // if no id, target all devices
                String cityId = a.get(2);
                String deviceId = null;

                // if id, target single device
                if (a.get(2).contains(":")) {
                    cityId = a.get(2).split(":")[0];
                    deviceId = a.get(2).split(":")[1];
                }

                cityMap.get(cityId).createSensorEvent(deviceId, type, value, subject);
            }
            default -> throw new ServiceException("create", "command not recognized");
        }
    }

    /**
     * Helper: handle define commands
     * Command must follow strict format as indicated
     * No optional arguments
     *
     * @param a command
     * @throws ServiceException if command cannot be processed
     */
    private void define(List<String> a) throws ServiceException {
        switch (a.get(1)) {
            // Expected Commands, all fields are required
            //   0            2             4               6             8           10             12
            // define city <city_id> name <name> account <address> lat <Float> long <Float> radius <Float>
            case "city" -> {
                if (a.size() != 13) {
                    throw new ServiceException("define city", "check number of command arguments!");
                }
                if (cityMap.containsKey(a.get(2))) {
                    throw new ServiceException("define city", "city id already exists!");
                }
                if(!a.get(3).equals("name") || !a.get(5).equals("account") || !a.get(7).equals("lat") || !a.get(9).equals("long") || !a.get(11).equals("radius")){
                    throw new ServiceException("define city", "command format error!");
                }
                City city = new City(a.get(2), a.get(4), a.get(6),
                        new Float[]{Float.parseFloat(a.get(8)), Float.parseFloat(a.get(10))},
                        Float.parseFloat(a.get(12)));
                cityMap.put(a.get(2), city);
            }
            //   0                        2                    4           6                  8             10
            // define street-sign <city_id>:<device_id> lat <Float> long <Float> enabled (true|false) text <text>
            case "street-sign" -> {
                if (a.size() != 11) {
                    throw new ServiceException("define street-sign", "check number of command arguments!");
                }
                if(!a.get(3).equals("lat") || !a.get(5).equals("long") || !a.get(7).equals("enabled") || !a.get(9).equals("text")){
                    throw new ServiceException("define street-sign", "command format error!");
                }
                cityMap.get(a.get(2).split(":")[0]).
                        defineStreetSign(a.get(2).split(":")[1],
                                new Float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                                a.get(8).equals("true"), a.get(10));
            }
            // define info-kiosk <city_id>:<device_id> lat <Float> long <Float> enabled (true|false) image <uri>
            case "info-kiosk" -> {
                if (a.size() != 11) {
                    throw new ServiceException("define info-kiosk", "check number of command arguments!");
                }
                if(!a.get(3).equals("lat") || !a.get(5).equals("long") || !a.get(7).equals("enabled") || !a.get(9).equals("image")){
                    throw new ServiceException("define info-kiosk", "command format error!");
                }
                cityMap.get(a.get(2).split(":")[0]).
                        defineInfoKiosk(a.get(2).split(":")[1],
                                new Float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                                a.get(8).equals("true"), a.get(10));
            }
            // define street-light <city_id>:<device_id> lat <Float> long <Float> enabled (true|false) brightness <int>
            case "street-light" -> {
                if (a.size() != 11) {
                    throw new ServiceException("define street-light", "check number of command arguments!");
                }
                if(!a.get(3).equals("lat") || !a.get(5).equals("long") || !a.get(7).equals("enabled") || !a.get(9).equals("brightness")){
                    throw new ServiceException("define street-light", "command format error!");
                }
                cityMap.get(a.get(2).split(":")[0]).
                        defineStreetLight(a.get(2).split(":")[1],
                                new Float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                                a.get(8).equals("true"), Integer.parseInt(a.get(10)));
            }
            // define parking-space <city_id>:<device_id> lat <Float> long <Float> enabled(true|false) rate <int>
            case "parking-space" -> {
                if (a.size() != 11) {
                    throw new ServiceException("define parking-space", "check number of command arguments!");
                }
                if(!a.get(3).equals("lat") || !a.get(5).equals("long") || !a.get(7).equals("enabled") || !a.get(9).equals("rate")){
                    throw new ServiceException("define parking-space", "command format error!");
                }
                cityMap.get(a.get(2).split(":")[0]).
                        defineParkingSpace(a.get(2).split(":")[1],
                                new Float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                                a.get(8).equals("true"), Integer.parseInt(a.get(10)));
            }
            // define robot <city_id>:<device_id> lat <Float> long <Float> enabled(true|false) activity <string>
            case "robot" -> {
                if (a.size() != 11) {
                    throw new ServiceException("define robot", "check number of command arguments!");
                }
                if(!a.get(3).equals("lat") || !a.get(5).equals("long") || !a.get(7).equals("enabled") || !a.get(9).equals("activity")){
                    throw new ServiceException("define robot", "command format error!");
                }
                cityMap.get(a.get(2).split(":")[0]).
                        defineRobot(a.get(2).split(":")[1],
                                new Float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                                a.get(8).equals("true"), a.get(10));
            }
            //   0      1             2                   4            6                8                10                12               14        16
            // define vehicle <city_id>:<device_id> lat <Float> long <Float> enabled(true|false) type (bus|car) activity <string> capacity <int> fee <int>
            case "vehicle" -> {
                if (a.size() != 17) {
                    throw new ServiceException("define vehicle", "check number of command arguments!");
                }
                if(!a.get(3).equals("lat") || !a.get(5).equals("long") || !a.get(7).equals("enabled") ||
                        !a.get(9).equals("type")|| !a.get(11).equals("activity")|| !a.get(13).equals("capacity")||
                        !a.get(15).equals("fee")){
                    throw new ServiceException("define vehicle", "command format error!");
                }
                cityMap.get(a.get(2).split(":")[0]).
                        defineVehicle(a.get(2).split(":")[1],
                                new Float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                                a.get(8).equals("true"), a.get(10), a.get(12),
                                Integer.parseInt(a.get(14)), Integer.parseInt(a.get(16)));
            }
            //   0       1         2              4                 6                 8                     10                        12         14                   16
            // define resident <person_id> name <name> bio-metric <string> phone <phone_number> role (adult|child|administrator) lat <lat> long <Float> account <account_address>
            case "resident" -> {
                if (a.size() != 17) {
                    throw new ServiceException("define resident", "check number of command arguments!");
                }
                if(!a.get(3).equals("name") || !a.get(5).equals("bio-metric") || !a.get(7).equals("phone") ||
                        !a.get(9).equals("role")|| !a.get(11).equals("lat")|| !a.get(13).equals("long")||
                        !a.get(15).equals("account")){
                    throw new ServiceException("define resident", "command format error!");
                }
                registry.definePerson(PersonType.resident, a.get(2), a.get(4), a.get(6), a.get(8), a.get(10),
                        new Float[]{Float.parseFloat(a.get(12)), Float.parseFloat(a.get(14))},
                        a.get(16));
            }
            //   0       1        2                     4           6          8
            // define visitor <person_id> bio-metric <string> lat <lat> long <Float>
            case "visitor" -> {
                if (a.size() != 9) {
                    throw new ServiceException("define visitor", "check number of command arguments!");
                }
                if(!a.get(3).equals("bio-metric") || !a.get(5).equals("lat") || !a.get(7).equals("long")){
                    throw new ServiceException("define street-light", "command format error!");
                }
                registry.definePerson(PersonType.visitor, a.get(2), null, a.get(4), null, null,
                        new Float[]{Float.parseFloat(a.get(6)), Float.parseFloat(a.get(8))},
                        null);
            }
            default -> throw new ServiceException("define", "subject not recognized!");
        }
    }

    /**
     * Helper: handles show commands
     *
     * @param a command
     * @throws ServiceException if show error
     */
    private void show(List<String> a) throws ServiceException {
        switch (a.get(1)) {
            // Expected Commands, device_id is optional
            // show city <city_id>
            case "city" -> {
                // show info
                if (cityMap.get(a.get(2)) == null){
                    throw new ServiceException("show city","city not found!");
                } else {
                    System.out.println(cityMap.get(a.get(2)));
                    System.out.println(" "); //line break
                }

                City c = cityMap.get(a.get(2));
                Float[] center = c.getLocation();
                Map<String, IoTDevice> allDevices = c.showAllDevices();

                // show devices
                for (String key : allDevices.keySet()) {
                    // only display if within city radius
                    if (distance(allDevices.get(key).getLocation(), center) <= c.getRadius()) {
                        System.out.println(key + "=" + allDevices.get(key));
                        System.out.println(" "); // line break
                    }
                }

                // show people
                Map<String, Person> allPersons = registry.showAllPersons();
                for (String personId : allPersons.keySet()) {
                    // only display if within city radius
                    if (distance(allPersons.get(personId).getLocation(), center) <= c.getRadius()) {
                        System.out.println(personId + "=" + allPersons.get(personId));
                        System.out.println(" "); // line break
                    }
                }
            }
            case "device" -> {
                if (a.get(2).contains(":")) {
                    // show single device
                    System.out.println(cityMap.get(a.get(2).split(":")[0]).showDevice(a.get(2).split(":")[1]));
                } else {
                    if (!cityMap.containsKey(a.get(2))) {
                        throw new ServiceException("show city devices", "city not found!");
                    }
                    // show all devices
                    Map<String, IoTDevice> allDevices = cityMap.get(a.get(2)).showAllDevices();
                    // System.out.println(cityMap.get(a.get(2)).showAllDevices());
                    for (String key : allDevices.keySet()) {
                        System.out.println(key + "=" + allDevices.get(key));
                        System.out.println(" "); // line break
                    }
                }
            }
            // show person <person_id>
            case "person" -> System.out.println(registry.showPerson(a.get(2)));
            default -> throw new ServiceException("show", "subject not found!");
        }
    }

    /**
     * Helper: handle update commands
     *
     * @param a command
     */
    private void update(List<String> a) throws ServiceException {

        String primaryId;
        String secondaryId;

        // look up id
        if (a.get(2).contains(":")) {
            primaryId = a.get(2).split(":")[0];
            secondaryId = a.get(2).split(":")[1];
        } else {
            primaryId = a.get(2);
            secondaryId = null;
        }

        // lookup attributes
        Boolean enabled = Objects.equals(findAttr(a, "enabled"), "true");
        Float lat = (findAttr(a, "lat") != null) ? Float.parseFloat(Objects.requireNonNull(findAttr(a, "lat"))) : null;
        Float lon = (findAttr(a, "long") != null) ? Float.parseFloat(Objects.requireNonNull(findAttr(a, "long"))) : null;
        Float[] location = new Float[]{lat, lon};
        if (lat == null || lon == null) {
            location = null;
        }
        String activity = findAttr(a, "activity");
        String biometric = findAttr(a, "biometric");

        switch (a.get(1)) {
            // Expected Commands, [bracketed] are optional
            //   0       1               2
            // update street-sign <city_id>:<device_id> [enabled (true|false)] [text <text>]
            case "street-sign" -> {
                String text = findAttr(a, "text");
                ((StreetSign) cityMap.get(primaryId).showDevice(secondaryId))
                        .updateStreetSign(enabled, text);
            }
            // update info-kiosk <city_id>:<device_id> [enabled (true|false)] [image <uri>]
            case "info-kiosk" -> {
                String uri = findAttr(a, "image");
                ((InfoKiosk) cityMap.get(primaryId).showDevice(secondaryId))
                        .updateInfoKiosk(enabled, uri);
            }
            // update street-light <city_id>:<device_id> [enabled (true|false)] [brightness<int>]
            case "street-light" -> {
                Integer brightness = findAttr(a, "brightness") != null ? Integer.parseInt(Objects.requireNonNull(findAttr(a, "brightness"))) : null;
                ((StreetLight) cityMap.get(primaryId).showDevice(secondaryId))
                        .updateStreetLight(enabled, brightness);
            }
            // update parking-space <city_id>:<device_id> [enabled (true|false)] [rate<int>]
            case "parking-space" -> {
                Integer rate = findAttr(a, "rate") != null ? Integer.parseInt(Objects.requireNonNull(findAttr(a, "rate"))) : null;
                ((ParkingSpace) cityMap.get(primaryId).showDevice(secondaryId))
                        .updateParkingSpace(enabled, rate);
            }
            // update robot <city_id>:<device_id> [lat <Float> long <Float>] [enabled(true|false)] [activity <string>]
            case "robot" -> ((Robot) cityMap.get(primaryId).showDevice(secondaryId))
                    .updateRobot(location, enabled, activity);
            // update vehicle <city_id>:<device_id> [lat <Float> long <Float>] [enabled(true|false)] [activity <string>] [fee <int>]
            case "vehicle" -> {
                Integer fee = findAttr(a, "fee") != null ? Integer.parseInt(Objects.requireNonNull(findAttr(a, "fee"))) : null;
                ((Vehicle) cityMap.get(primaryId).showDevice(secondaryId))
                        .updateVehicle(location, enabled, activity, fee);
            }
            // update resident <person_id> [name <name>] [bio-metric <string>] [phone<phone_number>] [role (adult|child|administrator)] [lat <lat> long <Float>] [account <account_address>]
            case "resident" -> {
                String name = findAttr(a, "name");
                String phoneNumber = findAttr(a, "phoneNumber");
                String role = findAttr(a, "role");
                String account = findAttr(a, "account");
                Role roleType = null;
                if (role != null) {
                    switch (role) {
                        case "adult" -> roleType = Role.adult;
                        case "child" -> roleType = Role.child;
                        case "administrator" -> roleType = Role.administrator;
                        default -> throw new ServiceException("define Person", "unrecognized role!");
                    }
                }
                registry.showPerson(primaryId).updateResident(name, biometric, phoneNumber, roleType, location, account);
            }
            // update visitor <person_id> [bio-metric <string>] [lat <lat> long <Float>]
            case "visitor" -> registry.showPerson(primaryId).updateVisitor(biometric, location);
            default -> throw new ServiceException("update", "subject not recognized!");
        }
    }

    /**
     * Helper: get attribute from command
     *
     * @param a    command
     * @param attr attribute name
     * @return attribute value
     */
    private String findAttr(List<String> a, String attr) {
        int Idx = a.indexOf(attr);
        if (Idx == -1) {
            return null;
        } else if (a.size() > Idx + 1) {
            return a.get(Idx + 1);
        } else {
            return null;
        }
    }

    /**
     * https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
     * Calculate distance between two points in latitude and longitude taking
     * Uses Haversine method as its base.
     */
    private double distance(Float[] locA, Float[] locB) {

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

        return Math.sqrt(distance);
    }
}
