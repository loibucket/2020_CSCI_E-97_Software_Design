package cscie97.smartcity.model;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandAPI {

    private final Map<String, City> cityMap = new HashMap<>();
    private final Map<String, Person> personMap = new HashMap<>();

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
        String subject = a.get(1);
        try {
            // create define update show
            switch (action) {
                case "create" -> {
                    //   0          1         2                     3      4                                       5       6
                    // create sensor-event <city_id>[:<device_id>] type (microphone|camera|thermometer|co2meter) value <string> [subject <person_id>]
                    // create sensor-output <city_id>[:<device_id>] type (speaker) value <string>
                    switch (subject) {
                        case "sensor-event","sensor-output" -> cityMap.get(a.get(2).split(":")[0])
                                .createSensorEvent(a.get(2).split(":")[1],a.get(4),a.get(6),a.get(8));
                        default -> System.out.println("command not recognized");
                    }//(String deviceId, String type, String event, String personId)
                }
                case "define" -> {
                    // Expected Commands
                    switch (subject) {
                        //   0            2             4               6             8           10             12
                        // define city <city_id> name <name> account <address> lat <float> long <float> radius <float>
                        case "city" -> {
                            City city = new City(a.get(2), a.get(4), a.get(6),
                                    new float[]{Float.parseFloat(a.get(8)), Float.parseFloat(a.get(10))},
                                    Float.parseFloat(a.get(12)));
                            cityMap.put(a.get(2), city);
                        }
                        //   0                        2                    4           6                  8             10
                        // define street-sign <city_id>:<device_id> lat <float> long <float> enabled (true|false) text <text>
                        case "street-sign" -> cityMap.get(a.get(2).split(":")[0]).
                                defineStreetSign(a.get(2).split(":")[1],
                                        new float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                                        a.get(8).equals("true"), a.get(10));
                        // define info-kiosk <city_id>:<device_id> lat <float> long <float> enabled (true|false) image <uri>
                        case "info-kiosk" -> cityMap.get(a.get(2).split(":")[0]).
                                defineInfoKiosk(a.get(2).split(":")[1],
                                        new float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                                        a.get(8).equals("true"), a.get(10));
                        // define street-light <city_id>:<device_id> lat <float> long <float> enabled (true|false) brightness <int>
                        case "street-light" -> cityMap.get(a.get(2).split(":")[0]).
                                defineStreetLight(a.get(2).split(":")[1],
                                        new float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                                        a.get(8).equals("true"), Integer.parseInt(a.get(10)));
                        // define parking-space <city_id>:<device_id> lat <float> long <float> enabled(true|false) rate <int>
                        case "parking-space" -> cityMap.get(a.get(2).split(":")[0]).
                                defineParkingSpace(a.get(2).split(":")[1],
                                        new float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                                        a.get(8).equals("true"), Integer.parseInt(a.get(10)));
                        // define robot <city_id>:<device_id> lat <float> long <float> enabled(true|false) activity <string>
                        case "robot" -> cityMap.get(a.get(2).split(":")[0]).
                                defineRobot(a.get(2).split(":")[1],
                                        new float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                                        a.get(8).equals("true"), a.get(10));
                        //   0      1             2                   4            6                8                10                12               14        16
                        // define vehicle <city_id>:<device_id> lat <float> long <float> enabled(true|false) type (bus|car) activity <string> capacity <int> fee <int>
                        case "vehicle" -> cityMap.get(a.get(2).split(":")[0]).
                                defineVehicle(a.get(2).split(":")[1],
                                        new float[]{Float.parseFloat(a.get(4)), Float.parseFloat(a.get(6))},
                                        a.get(8).equals("true"), a.get(10), a.get(12),
                                        Integer.parseInt(a.get(14)), Integer.parseInt(a.get(16)));
                        //   0       1         2              4                 6                 8                     10                        12         14                   16
                        // define resident <person_id> name <name> bio-metric <string> phone <phone_number> role (adult|child|administrator) lat <lat> long <long> account <account_address>
                        case "resident" -> definePerson(PersonType.resident, a.get(2), a.get(4), a.get(6), a.get(8), a.get(10),
                                new float[]{Float.parseFloat(a.get(12)), Float.parseFloat(a.get(14))},
                                a.get(16));
                        //   0       1        2                     4           6          8
                        // define visitor <person_id> bio-metric <string> lat <lat> long <long>
                        case "visitor" -> definePerson(PersonType.visitor, a.get(2), null, a.get(4), null, null,
                                new float[]{Float.parseFloat(a.get(6)), Float.parseFloat(a.get(8))},
                                null);
                        default -> System.out.println("subject not recognized");
                    }
                }
                case "show" -> {
                    switch (subject) {
                        // show city <city_id>
                        case "city" -> System.out.println(cityMap.get(a.get(2)));
                        // show device <city_id>[:<device_id>]
                        case "device" -> System.out.println(cityMap.get(a.get(2).split(":")[0]).
                                showDevice(a.get(2).split(":")[1]));
                        // show person <person_id>
                        case "person" -> System.out.println(personMap.get(a.get(2)));
                        default -> System.out.println("subject not recognized");
                    }
                }
                case "update" -> {
                    System.out.println("update");
                    // update street-sign <city_id>:<device_id> [enabled (true|false)] [text <text>]
                    // update info-kiosk <city_id>:<device_id> [enabled (true|false)] [image <uri>]
                    // update street-light <city_id>:<device_id> [enabled (true|false)] [brightness<int>]
                    // update parking-space <city_id>:<device_id> [enabled (true|false)] [rate<int>]
                    // update robot <city_id>:<device_id> [lat <float> long <float>] [enabled(true|false)] [activity <string>]
                    // update vehicle <city_id>:<device_id> [lat <float> long <float>] [enabled(true|false)] [activity <string>] [fee <int>]
                    // update resident <person_id> [name <name>] [bio-metric <string>] [phone<phone_number>] [role (adult|child|administrator)] [lat <lat> long <long>] [account <account_address>]
                    // update visitor <person_id> [bio-metric <string>] [lat <lat> long <long>]
                    switch (subject) {
                        case "street-sign" -> System.out.println("street-sign");
                        case "info-kiosk" -> System.out.println("info-kiosk");
                        case "street-light" -> System.out.println("street-light");
                        case "parking-space" -> System.out.println("parking-space");
                        case "robot" -> System.out.println("robot");
                        case "vehicle" -> System.out.println("vehicle");
                        case "resident" -> System.out.println("resident");
                        case "visitor" -> System.out.println("visitor");
                        default -> System.out.println("subject not recognized");
                    }
                }
                default -> System.out.println("command not recognized");
            }
        } catch (CityException e) {
            // print error message, and continue processing next line
            System.out.println(new CommandAPIException(e.action, e.reason, lineNumber));
        }
    }

    public void processCommandFile(String authToken, String commandFile) {

        if (!"placeholder".equals(authToken)) {
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
            System.out.println(e);
        }
    }

    private void definePerson(PersonType type, String personId, String name, String biometricId, String phone, String role,
                             float[] location, String blockchainAddress) throws CityException{
        if (personMap.containsKey(personId)) {
            throw new CityException("define Person", "personId already exists!");
        }
        Role roleType = null;
        if (type == PersonType.resident){
            switch (role) {
                case "adult" -> roleType = Role.adult;
                case "child" -> roleType = Role.child;
                case "administrator" -> roleType = Role.administrator;
                default -> throw new CityException("define Person", "unrecognized role!");
            }
        }
        Person person = new Person(type, personId, name, biometricId, phone, roleType, location, blockchainAddress);
        personMap.put(personId,person);
    }

}
