package cscie97.smartcity.model;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandAPI {

	private Map<String,City> cityMap = new HashMap<String,City>();

	public void processCommand(String authToken, String command, int lineNumber) {

		System.out.println(command);

		// replace special quotes to normal
		command = command.replace('“', '"');
		command = command.replace('”', '"');

		// split string by space, except when between quotes
		// stackoverflow.com/questions/18893390/splitting-on-comma-outside-quotes
		List<String> a = Arrays.asList(command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));

		// do different action with commands
		String action = a.get(0);
		String subject = a.get(1);
		try {
			// create define update show
			// create define update show
			switch (action) {
				case "create" -> {
					System.out.println("create");
					// create sensor-event <city_id>[:<device_id>] type (microphone|camera|thermometer|co2meter) value <string> [subject <person_id>]
					// create sensor-output <city_id>[:<device_id>] type (speaker) value <string>
					switch (subject) {
						case "sensor-event" -> System.out.println("sensor-event");
						case "sensor-output" -> System.out.println("sensor-output");
						default -> System.out.println("command not recognized");
					}
				}
				case "define" -> {
					System.out.println("define");
					// Expected Commands
					//   0            2             4               6             8           10             12
					// define city <city_id> name <name> account <address> lat <float> long <float> radius <float>
					// define street-sign <city_id>:<device_id> lat <float> long <float> enabled (true|false) text <text>
					// define info-kiosk <city_id>:<device_id> lat <float> long <float> enabled (true|false) image <uri>
					// define street-sign <city_id>:<device_id> lat <float> long <float> enabled (true|false) text <text>
					// define parking-space <city_id>:<device_id> lat <float> long <float> enabled(true|false) rate <int>
					// define robot <city_id>:<device_id> lat <float> long <float> enabled(true|false) activity <string>
					// define vehicle <city_id>:<device_id> lat <float> long <float> enabled(true|false) type (bus|car) activity <string> capacity <int> fee <int>
					// define resident <person_id> name <name> bio-metric <string> phone <phone_number> role (adult|child|administrator) lat <lat> long <long> account <account_address>
					// define visitor <person_id> bio-metric <string> lat <lat> long <long>
					switch (subject) {
						case "city" -> {
							City city = new City(a.get(2),a.get(4),a.get(6),
									new float[]{Float.parseFloat(a.get(8)), Float.parseFloat(a.get(10))},
									Float.parseFloat(a.get(12)));
							cityMap.put(a.get(2),city);
						}
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
				case "show" -> {
					System.out.println("show");
					// show city <city_id>
					// show device <city_id>[:<device_id>]
					// show person <person_id>
					switch (subject) {
						case "city" -> System.out.println("city");
						case "device" -> System.out.println("device");
						case "person" -> System.out.println("person");
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
		} catch (Exception e) {
			// print error message, and continue processing next line
			// System.out.println(new CommandAPIException(e.action, e.reason, lineNumber));
		}
	}

	public void processCommandFile(String authToken, String commandFile) {

		if(!"placeholder".equals(authToken)){
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
			//System.out.println(e);
		}
	}
}
