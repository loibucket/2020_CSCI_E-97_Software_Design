package cscie97.smartcity.test;

import cscie97.smartcity.model.*;
import java.lang.String;
import java.util.*;

/**
 * Process a set of command in a text file specified when TestDriver is
 * called, or process user interactive input if no file specified
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class TestDriver {
    public static void main(String[] args) {

        CommandAPI commandAPI = new CommandAPI();

        if (args.length == 0){
            // interactive mode
            Scanner sc = new Scanner(System.in);
            System.out.println("SMART CITY");
            System.out.println("CTRL-C TO EXIT");
            try {
                while (true) {
                    System.out.print("COMMAND:");
                    String str = sc.nextLine();
                    commandAPI.processCommand("placeholder", str,-1);
                }
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("FINISHED");
                sc.close();
            }
        } else {
            // process file
            commandAPI.processCommandFile("placeholder",args[0]);
        }
    }
}