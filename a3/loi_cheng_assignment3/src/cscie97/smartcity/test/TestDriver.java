package cscie97.smartcity.test;

import cscie97.ledger.CommandProcessor;
import cscie97.smartcity.controller.Command;
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

//        //initialize a test-ledger
//        try {
//            //create ledger
//            CommandProcessor.processCommand("create-ledger test description \"test ledger 2020\" seed \"harvard\"", -500);
//            //create accounts
//            CommandProcessor.processCommand("create-account janes_account", -1);
//            CommandProcessor.processCommand("create-account joes_account", -1);
//            CommandProcessor.processCommand("create-account resident_3_account", -1);
//            CommandProcessor.processCommand("create-account resident_4_account", -1);
//            CommandProcessor.processCommand("create-account resident_5_account", -1);
//            CommandProcessor.processCommand("create-account resident_6_account", -1);
//            CommandProcessor.processCommand("create-account resident_7_account", -1);
//            CommandProcessor.processCommand("create-account resident_8_account", -1);
//            CommandProcessor.processCommand("create-account resident_9_account", -1);
//            CommandProcessor.processCommand("create-account resident_A_account", -1);
//            //add balances
//            CommandProcessor.processCommand("process-transaction 1 amount 1000 fee 10 note \"fund account\" payer master receiver janes_account", -1);
//            CommandProcessor.processCommand("process-transaction 1 amount 1000 fee 10 note \"fund account\" payer master receiver joes_account", -1);
//            CommandProcessor.processCommand("process-transaction 1 amount 1000 fee 10 note \"fund account\" payer master receiver resident_3_account", -1);
//            CommandProcessor.processCommand("process-transaction 1 amount 1000 fee 10 note \"fund account\" payer master receiver resident_4_account", -1);
//            CommandProcessor.processCommand("process-transaction 1 amount 1000 fee 10 note \"fund account\" payer master receiver resident_5_account", -1);
//            CommandProcessor.processCommand("process-transaction 1 amount 1000 fee 10 note \"fund account\" payer master receiver resident_6_account", -1);
//            CommandProcessor.processCommand("process-transaction 1 amount 1000 fee 10 note \"fund account\" payer master receiver resident_7_account", -1);
//            CommandProcessor.processCommand("process-transaction 1 amount 1000 fee 10 note \"fund account\" payer master receiver resident_8_account", -1);
//            CommandProcessor.processCommand("process-transaction 1 amount 1000 fee 10 note \"fund account\" payer master receiver resident_9_account", -1);
//            CommandProcessor.processCommand("process-transaction 1 amount 1000 fee 10 note \"fund account\" payer master receiver resident_A_account", -1);
//            //check transactions
//            CommandProcessor.processCommand("get-account-balances", -1);
//            CommandProcessor.processCommand("get-block 1", -1);
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }

        if (args.length == 0) {
            // interactive mode
            Scanner sc = new Scanner(System.in);
            System.out.println("SMART CITY");
            System.out.println("CTRL-C TO EXIT");
            try {
                while (true) {
                    System.out.print("COMMAND:");
                    String str = sc.nextLine();
                    CommandAPI.processCommand("placeholder", str, -1);
                }
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("FINISHED");
                sc.close();
            }
        } else if (args.length == 1) {
            // process a city service script
            CommandAPI.processCommandFile("placeholder", args[0]);
        } else if (args.length == 2) {
            // process a ledger script and then a city service script
            CommandProcessor.processCommandFile(args[0]);
            CommandAPI.processCommandFile("placeholder", args[1]);
        }
    }
}