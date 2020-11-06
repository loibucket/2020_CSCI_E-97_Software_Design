package cscie97.smartcity.shared;

import cscie97.smartcity.authenticator.AuthToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.atomic.AtomicInteger;

public class FileProcessor {

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
                        //CommandAPI.processCommand(authToken, line, lineNumber.get());
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

