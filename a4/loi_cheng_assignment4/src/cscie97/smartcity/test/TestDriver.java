package cscie97.smartcity.test;

import cscie97.ledger.CommandProcessor;
import cscie97.smartcity.model.*;

import java.lang.String;
import java.util.*;

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
    /**
     * Main class, reads commands and sends to API
     *
     * @param args command script, or none if interactive input
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            // interactive mode
            Scanner sc = new Scanner(System.in);
            System.out.println("SMART CITY");
            System.out.println("CTRL-C TO EXIT");
            try {
                while (true) {
                    System.out.print("COMMAND:");
                    String str = sc.nextLine();
                    ModelAPI.processCommand(null, str, -1);
                }
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("FINISHED");
                sc.close();
            }
        } else if (args.length == 1) {
            // process a city service script
            ModelAPI.processCommandFile(null, args[0]);
        } else if (args.length == 2) {
            // process a ledger script and then a city service script
            CommandProcessor.processCommandFile(null, args[0]);
            ModelAPI.processCommandFile(null, args[1]);
        }
    }
}