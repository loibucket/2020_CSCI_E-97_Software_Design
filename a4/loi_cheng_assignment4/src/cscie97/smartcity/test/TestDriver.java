package cscie97.smartcity.test;

import cscie97.ledger.LedgerApi;
import cscie97.ledger.LedgerApiException;
import cscie97.smartcity.authenticator.AuthenticationApi;
import cscie97.smartcity.model.*;
import cscie97.smartcity.shared.FileProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.String;
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

    private static String mode = "modelapi"; // or ledgerapi or authapi or mainapi
    private static String read = "line"; // or readline

    /**
     * Main class, reads commands and sends to API
     *
     * @param args command script, or none if interactive input
     */
    public static void main(String[] args) {

        System.out.println("SMART CITY");
        if (args.length >= 2) {
            // digest file if provided
            digest(args[0]);
            // continue with interactive mode
        } else if (args.length == 1) {
            // digest file if provided
            digest(args[0]);
            return;
        }

        // continue with interactive mode
        Scanner sc = new Scanner(System.in);
        System.out.println("CTRL-C TO EXIT");
        try {
            while (true) {
                System.out.print(mode.toUpperCase() + " " + read.toUpperCase() + ":");
                String str = sc.nextLine();
                process(str);
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
                System.out.print(mode.toUpperCase() + " " + read.toUpperCase() + ": ");
                System.out.println(line);
                if (line.charAt(0) != "#".charAt(0)) {
                    process(line);
                }
                lineNumber.getAndIncrement();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private static void process(String str) {

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

        //toggle api mode
        switch (str.toLowerCase()) {
            case "modelapi" -> {
                mode = "modelapi";
                return;
            }
            case "ledgerapi" -> {
                mode = "ledgerapi";
                return;
            }
            case "authapi" -> {
                mode = "authapi";
                return;
            }
        }

        //process the line based on active mode
        if (read.equals("line")) {
            //run processor
            switch (mode) {
                case "modelapi" -> ModelApi.processCommand(null, str, -1);
                case "ledgerapi" -> LedgerApi.processCommand(null, str, -1);
                case "authapi" -> AuthenticationApi.processCommand(null, str, -1);
            }
        } else { //if (read.equals("file")) {
            FileProcessor.processCommandFile(null, str, mode);
        }

    }
}