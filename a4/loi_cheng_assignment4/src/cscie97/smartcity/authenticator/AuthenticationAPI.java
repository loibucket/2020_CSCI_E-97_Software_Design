package cscie97.smartcity.authenticator;

import cscie97.smartcity.shared.FileProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AuthenticationAPI {

    /**
     * process a line of command
     *
     * @param command    command
     * @param lineNumber line number if given
     */
    public static void processCommand(AuthToken authToken, String command, int lineNumber) {

        System.out.println("COMMAND: " + command);
        System.out.println("RESPONSE: ");

        // replace special quotes to normal
        command = command.replace('“', '"');
        command = command.replace('”', '"');

        // split string by whitespace, except when between quotes
        // stackoverflow.com/questions/18893390/splitting-on-comma-outside-quotes
        List<String> a = Arrays.asList(command.split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));

        // do different action with commands
        String action = a.get(0);

        try {
            switch (action) {
                case "define_permission" -> Authenticator.definePermission(a.get(1), a.get(2), a.get(3));
                case "define_role" -> Authenticator.defineRole(a.get(1), a.get(2), a.get(3));
                case "add_permission_to_role", "add_role_to_user" -> Authenticator.addSubAuth(a.get(1), a.get(2));
                case "create_user" -> Authenticator.createResource(a.get(1), a.get(2), null);
                case "add_user_credential" -> Authenticator.addUserCredential(a.get(1), a.get(2), a.get(3));
                case "create_resource_role" -> Authenticator.createResourceRole(a.get(1), a.get(2), a.get(3));
                default -> throw new AccessException("process command", "command not recognized");
            }
        } catch (Exception | AccessException e) {
            System.out.println(e.toString());
        }

        System.out.println(":END"); //end command
        System.out.println(" "); //line break
    }

    /**
     * process a set of commands from file
     *
     * @param authToken   authorization token
     * @param commandFile command file name
     */
    public static void processCommandFile(AuthToken authToken, String commandFile) {

//        if (!authToken.equals("placeholder")) {
//            System.out.println("authentication error");
//            return;
//        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(commandFile));
            AtomicInteger lineNumber = new AtomicInteger(1);
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (line.charAt(0) != "#".charAt(0)) {
                        processCommand(authToken, line, lineNumber.get());
                    } else {
                        System.out.println("# LINE " + lineNumber.get() + " " + line);
                    }
                }
                lineNumber.getAndIncrement();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
