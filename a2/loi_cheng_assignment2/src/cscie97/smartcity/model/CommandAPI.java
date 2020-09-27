package cscie97.smartcity.model;

import java.io.*;
import java.util.List;
import java.util.Arrays;

public class CommandAPI {

	private int cityMap;

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
			switch (action) {
				case "create":
					System.out.println("create");
					switch (subject) {
						case "sensor-event":
							// create sensor-event <city_id>[:<device_id>] type (microphone|camera|thermometer|co2meter) value <string> [subject <person_id>]
							System.out.println("sensor-event");
							break;
						case "sensor-output":
							// create sensor-output <city_id>[:<device_id>] type (speaker) value <string>
							System.out.println("sensor-output");
							break;
						default:
							System.out.println("command not recognized");
							break;
					}
					break;
				case "define":
					System.out.println("define");
					switch (subject) {
						case "city":
							// define city <city_id> name <name> account <address> lat <float> long <float> radius <float>
							System.out.println("city");
							break;
						case "street-sign":
							//define street-sign <city_id>:<device_id> lat <float> long <float> enabled (true|false) text <text>
							System.out.println("street-sign");
							break;
						case "info-kiosk":
							//define info-kiosk <city_id>:<device_id> lat <float> long <float> enabled (true|false) image <uri>
							System.out.println("info-kiosk");
							break;
						case "street-light":
							//define street-sign <city_id>:<device_id> lat <float> long <float> enabled (true|false) text <text>
							System.out.println("street-light");
							break;
						case "parking-space":
							// define parking-space <city_id>:<device_id> lat <float> long <float> enabled(true|false) rate <int>
							System.out.println("parking-space");
							break;
						case "robot":
							// define robot <city_id>:<device_id> lat <float> long <float> enabled(true|false) activity <string>
							System.out.println("robot");
							break;
						case "vehicle":
							// define vehicle <city_id>:<device_id> lat <float> long <float> enabled(true|false) type (bus|car) activity <string> capacity <int> fee <int>
							System.out.println("vehicle");
							break;
						case "resident":
							// define resident <person_id> name <name> bio-metric <string> phone <phone_number> role (adult|child|administrator) lat <lat> long <long> account <account_address>
							System.out.println("resident");
							break;
						case "visitor":
							// define visitor <person_id> bio-metric <string> lat <lat> long <long>
							System.out.println("visitor");
							break;
						default:
							System.out.println("subject not recognized");
							break;
					}
					break;
				case "show":
					System.out.println("show");
					switch (subject) {
						case "city":
							// show city <city_id>
							System.out.println("city");
							break;
						case "device":
							// show device <city_id>[:<device_id>]
							System.out.println("device");
							break;
						case "person":
							// show person <person_id>
							System.out.println("person");
							break;
						default:
							System.out.println("subject not recognized");
							break;
					}
					break;
				case "update":
					System.out.println("update");
					switch (subject) {
						case "street-sign":
							// update street-sign <city_id>:<device_id> [enabled (true|false)] [text <text>]
							System.out.println("street-sign");
							break;
						case "info-kiosk":
							// update info-kiosk <city_id>:<device_id> [enabled (true|false)] [image <uri>]
							System.out.println("info-kiosk");
							break;
						case "street-light":
							// update street-light <city_id>:<device_id> [enabled (true|false)] [brightness<int>]
							System.out.println("street-light");
							break;
						case "parking-space":
							// update parking-space <city_id>:<device_id> [enabled (true|false)] [rate<int>]
							System.out.println("parking-space");
							break;
						case "robot":
							// update robot <city_id>:<device_id> [lat <float> long <float>] [enabled(true|false)] [activity <string>]
							System.out.println("robot");
							break;
						case "vehicle":
							// update vehicle <city_id>:<device_id> [lat <float> long <float>] [enabled(true|false)] [activity <string>] [fee <int>]
							System.out.println("vehicle");
							break;
						case "resident":
							// update resident <person_id> [name <name>] [bio-metric <string>] [phone<phone_number>] [role (adult|child|administrator)] [lat <lat> long <long>] [account <account_address>]
							System.out.println("resident");
							break;
						case "visitor":
							// update visitor <person_id> [bio-metric <string>] [lat <lat> long <long>]
							System.out.println("visitor");
							break;
						default:
							System.out.println("subject not recognized");
							break;
					}
					break;
				// create define update show
				default:
					System.out.println("command not recognized");
			}
		} catch (Exception e) {
			// print error message, and continue processing next line
			System.out.println(e);
		}
	}

	public void processCommandFile(String authToken, String commandFile) {

		if(authToken != "placeholder"){
			System.out.println("authentication error");
			return;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(commandFile));
			String line;
			int lineNumber = 1;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					// pass on empty line
				} else if (line.charAt(0) == "#".charAt(0)) {
					// pass on comment
				} else {
					processCommand(authToken, line, lineNumber);
				}
				lineNumber++;
			}
			reader.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
