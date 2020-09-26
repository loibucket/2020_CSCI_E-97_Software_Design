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
		try {
			switch (action) {
				case "create-ledger":
					System.out.println("a");
					break;
				case "create-account":
					System.out.println("b");
					break;
				default:
					System.out.println("c");
			}
		} catch (Exception e) {
			// print error message, and continue processing next line
			System.out.println(e);
		}
	}

	public void processCommandFile(String authToken, String commandFile) {
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
