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

    private final Map<String, City> cityMap = new HashMap<>();
    private final Map<String, Person> personMap = new HashMap<>();

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
                default -> System.out.println("command not recognized");
            }
        } catch (ServiceException e) {
            System.out.println(new CommandException(command, e.action, e.reason, lineNumber).toString());
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            System.out.println(new CommandException(command, null, "too few arguments!", lineNumber).toString());
        } catch (Exception e) {
            System.out.println(new CommandException(command, null, "unspecified", lineNumber).toString());
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
            default -> System.out.println("command not recognized");
        }
    }

    /**
     * Helper: handle define commands
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
                City city = new City(a.get(2), a.get(4), a.get(6),
                        new Float[]{Float.parseFloat(a.get(8)), Float.parseFloat(a.get(10))},
                        Float.parseFloat(a.get(12)));
                cityMap.put(a.get(2), city);
            }
            //   0                        2                    4           6                  8             10
            // define street-sign <city_id>:<device_id> lat <Float> long <Float> enabled (true|false) text <text>
            case "street-sign" -> cityMap.get(a.get(2).split(":")[0]).
                    defineStreetSign(a.get(2).split(":")[1],
                            new Float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                            a.get(8).equals("true"), a.get(10));
            // define info-kiosk <city_id>:<device_id> lat <Float> long <Float> enabled (true|false) image <uri>
            case "info-kiosk" -> cityMap.get(a.get(2).split(":")[0]).
                    defineInfoKiosk(a.get(2).split(":")[1],
                            new Float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                            a.get(8).equals("true"), a.get(10));
            // define street-light <city_id>:<device_id> lat <Float> long <Float> enabled (true|false) brightness <int>
            case "street-light" -> cityMap.get(a.get(2).split(":")[0]).
                    defineStreetLight(a.get(2).split(":")[1],
                            new Float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                            a.get(8).equals("true"), Integer.parseInt(a.get(10)));
            // define parking-space <city_id>:<device_id> lat <Float> long <Float> enabled(true|false) rate <int>
            case "parking-space" -> cityMap.get(a.get(2).split(":")[0]).
                    defineParkingSpace(a.get(2).split(":")[1],
                            new Float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                            a.get(8).equals("true"), Integer.parseInt(a.get(10)));
            // define robot <city_id>:<device_id> lat <Float> long <Float> enabled(true|false) activity <string>
            case "robot" -> cityMap.get(a.get(2).split(":")[0]).
                    defineRobot(a.get(2).split(":")[1],
                            new Float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                            a.get(8).equals("true"), a.get(10));
            //   0      1             2                   4            6                8                10                12               14        16
            // define vehicle <city_id>:<device_id> lat <Float> long <Float> enabled(true|false) type (bus|car) activity <string> capacity <int> fee <int>
            case "vehicle" -> cityMap.get(a.get(2).split(":")[0]).
                    defineVehicle(a.get(2).split(":")[1],
                            new Float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                            a.get(8).equals("true"), a.get(10), a.get(12),
                            Integer.parseInt(a.get(14)), Integer.parseInt(a.get(16)));
            //   0       1         2              4                 6                 8                     10                        12         14                   16
            // define resident <person_id> name <name> bio-metric <string> phone <phone_number> role (adult|child|administrator) lat <lat> long <Float> account <account_address>
            case "resident" -> definePerson(PersonType.resident, a.get(2), a.get(4), a.get(6), a.get(8), a.get(10),
                    new Float[]{Float.parseFloat(a.get(12)), Float.parseFloat(a.get(14))},
                    a.get(16));
            //   0       1        2                     4           6          8
            // define visitor <person_id> bio-metric <string> lat <lat> long <Float>
            case "visitor" -> definePerson(PersonType.visitor, a.get(2), null, a.get(4), null, null,
                    new Float[]{Float.parseFloat(a.get(6)), Float.parseFloat(a.get(8))},
                    null);
            default -> System.out.println("subject not recognized");
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
                System.out.println(cityMap.get(a.get(2)));
                System.out.println(" "); //line break

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
                for (String key : personMap.keySet()) {
                    // only display if within city radius
                    if (distance(personMap.get(key).getLocation(), center) <= c.getRadius()) {
                        System.out.println(key + "=" + personMap.get(key));
                        System.out.println(" "); // line break
                    }
                }
            }
            case "device" -> {
                if (a.get(2).contains(":")) {
                    // target single device
                    System.out.println(
                            cityMap.get(a.get(2).split(":")[0]).showDevice(a.get(2).split(":")[1])
                    );
                } else {
                    // target all devices
                    Map<String, IoTDevice> allDevices = cityMap.get(a.get(2)).showAllDevices();
                    // System.out.println(cityMap.get(a.get(2)).showAllDevices());
                    for (String key : allDevices.keySet()) {
                        System.out.println(key + "=" + allDevices.get(key));
                        System.out.println(" "); // line break
                    }
                }
            }
            // show person <person_id>
            case "person" -> System.out.println(personMap.get(a.get(2)));
            default -> System.out.println("subject not recognized");
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
                personMap.get(primaryId).updateResident(name, biometric, phoneNumber, roleType, location, account);
            }
            // update visitor <person_id> [bio-metric <string>] [lat <lat> long <Float>]
            case "visitor" -> personMap.get(primaryId).updateVisitor(biometric, location);
            default -> System.out.println("subject not recognized");
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
     * Helper: Define a person
     *
     * @param type              resident or visitor
     * @param personId          id
     * @param name              name
     * @param biometricId       biometric id
     * @param phone             phone number
     * @param role              adult child administrator
     * @param location          latitude longitude
     * @param blockchainAddress account address
     * @throws ServiceException if unable to process
     */
    private void definePerson(PersonType type, String personId, String name, String biometricId, String phone,
                              String role, Float[] location, String blockchainAddress) throws ServiceException {

        // error
        if (personMap.containsKey(personId)) {
            throw new ServiceException("define Person", "personId already exists!");
        }

        // convert role to enum
        Role roleType = null;
        if (type == PersonType.resident) {
            switch (role) {
                case "adult" -> roleType = Role.adult;
                case "child" -> roleType = Role.child;
                case "administrator" -> roleType = Role.administrator;
                default -> throw new ServiceException("define Person", "unrecognized role!");
            }
        }

        // create person and add to map
        Person person = new Person(type, personId, name, biometricId, phone, roleType, location, blockchainAddress);
        personMap.put(personId, person);
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
