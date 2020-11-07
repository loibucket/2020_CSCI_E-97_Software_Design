package cscie97.smartcity.shared;

import cscie97.ledger.LedgerApi;
import cscie97.smartcity.authenticator.AuthToken;
import cscie97.smartcity.authenticator.AuthenticationApi;
import cscie97.smartcity.model.ModelApi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.atomic.AtomicInteger;

public class FileProcessor {

    public static void processCommandFile(AuthToken authToken, String commandFile, String apiMode) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(commandFile));
            AtomicInteger lineNumber = new AtomicInteger(1);
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (line.charAt(0) != "#".charAt(0)) {
                        switch (apiMode) {
                            case "modelapi" -> ModelApi.processCommand(authToken, line, lineNumber.get());
                            case "ledgerapi" -> LedgerApi.processCommand(authToken, line, lineNumber.get());
                            case "authapi" -> AuthenticationApi.processCommand(authToken, line, lineNumber.get());
                            default -> {/*no action*/}
                        }
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

