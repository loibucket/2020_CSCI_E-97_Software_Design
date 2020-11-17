package cscie97.smartcity.test;

import cscie97.ledger.LedgerApi;
import cscie97.ledger.LedgerApiException;
import cscie97.smartcity.authenticator.AuthException;
import cscie97.smartcity.authenticator.AuthenticationApi;
import cscie97.smartcity.authenticator.Authenticator;
import cscie97.smartcity.model.*;
import cscie97.smartcity.shared.FileProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.String;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Process a set of command in a text file specified when TestDriver is
 * called, or process user interactive input if no file specified
 * <p>
 * v1.0 2020-09-29 initial
 * v1.1 2020-10-19 updated command api implementation to static
 *
 * @author Loi Cheng
 * @version 1.1
 * @since 2020-10-19
 */
public class TestDriver {

    private static String read = "line"; // or readline

    /**
     * Main class, reads commands and sends to API
     *
     * @param args command script, or none if interactive input
     */
    public static void main(String[] args) {

        // env file is processed in boostrap mode
        if (Authenticator.getBootStrapMode()) {
            URL url = TestDriver.class.getResource("env.txt");
            //File file = new File(url.getPath());
            FileProcessor.processCommandFile(url.getPath());
            Authenticator.endBootStrapMode();
        }

        System.out.println("SMART CITY");

        if (args.length >= 1) {
            // digest file
            digest(args[0]);
            return;
        }

        // no args, interactive mode
        Scanner sc = new Scanner(System.in);
        System.out.println("CTRL-C TO EXIT");
        try {
            while (true) {
                System.out.print(read.toUpperCase() + ":");
                String str = sc.nextLine();
                // process(str);
                ModelApi.processCommand(str, -1);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("FINISHED");
            sc.close();
        }
    }

    private static void digest(String fileName) {
        // read commands from file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            AtomicInteger lineNumber = new AtomicInteger(1);
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.print(read.toUpperCase() + ": ");
                System.out.println(line);
                if (line.charAt(0) != "#".charAt(0)) {
                    process(line);
                }
                lineNumber.getAndIncrement();
            }
            reader.close();
        } catch (Exception | AuthException e) {
            System.out.println(e.toString());
        }
    }

    private static void process(String str) throws AuthException, NoSuchAlgorithmException {

        //digest another file -- overrides all active modes
        if (str.split(" ")[0].equals("digest")) {
            try {
                digest(str.split(" ")[1]);
                return;
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        //toggle file mode or command line
        switch (str.toLowerCase()) {
            case "file" -> {
                read = "file";
                return;
            }
            case "line" -> {
                read = "line";
                return;
            }
        }

        //process a single line, or process an entire file, based on active mode
        if (read.equals("line")) {
            ModelApi.processCommand(str, -1);
        } else { //if (read.equals("file")) {
            FileProcessor.processCommandFile(str);
        }
    }
}