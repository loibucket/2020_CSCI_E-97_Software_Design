package com.cscie97.ledger;

import java.io.*;
import java.util.List;
import java.util.Arrays;

/**
 * The CommandProcessor is a utility class for feeding the Ledger a set of
 * operations, using command syntax. The command syntax specification follows:
 *
 * Create Ledger Create a new ledger with the given name, description, and seed
 * value.
 *
 * create-ledger <name> description <description> seed <seed>
 *
 * Create Account Create a new account with the given account id.
 *
 * create-account <account-id>
 *
 * Process Transaction Process a new transaction. Return an error message if the
 * transaction is invalid, otherwise output the transaction id.
 *
 * process-transaction <transaction-id> amount <amount> fee <fee> note <note>
 * payer <account-address> receiver <account-address>
 *
 * Get Account Balance Output the account balance for the given account
 * get-account-balance <account-id>
 *
 * Get Account Balances Output the account balances for the most recent
 * completed block.
 *
 * get-account-balances
 *
 * Get Block Output the details for the given block number.
 *
 * get-block <block-number>
 *
 * Get Transaction Output the details of the given transaction id. Return an
 * error if the transaction was not found in the Ledger.
 *
 * get-transaction <transaction-id>
 *
 * Validate
 *
 * Validate the current state of the blockchain. validate Comment lines are
 * denoted with a # in the first column.
 *
 * validate
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-05
 */
public class CommandProcessor {

    // the ledger, this is static so there can only be 1 ledger
    private static Ledger ledger = null;

    /**
     * Process a single command. The output of the command is formatted and
     * displayed to stdout. Throw a CommandProcessorException on error.
     *
     * @param command
     * @param lineNumber
     * @throws CommandProcessorException
     */
    public static void processCommand(String command, int lineNumber) throws CommandProcessorException {

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
                    ledger = new Ledger(a.get(1), a.get(3), a.get(4));
                    break;
                case "create-account":
                    ledger.createAccount(a.get(1));
                    break;
                case "process-transaction":
                    // use helper function procTransHelp
                    procTransHelp(a, lineNumber);
                    break;
                case "get-account-balance":
                    System.out.println(ledger.getAccountBalance(a.get(1)));
                    break;
                case "get-account-balances":
                    System.out.println(ledger.getAccountBalances());
                    break;
                case "get-block":
                    System.out.println(ledger.getBlock(Integer.parseInt(a.get(1))));
                    break;
                case "get-transaction":
                    System.out.println(ledger.getTransaction(a.get(1)).toString().replace("\n", ""));
                    break;
                case "Validate":
                    ledger.validate();
                    System.out.println("ledger is valid");
                    break;
                default:
                    System.out.println(new CommandProcessorException(action, "no such command", lineNumber));
            }
        } catch (LedgerException e) {
            // print error message, and continue processing next line
            System.out.println(new CommandProcessorException(e.action, e.reason, lineNumber));
        }
    }

    /**
     * Process a set of commands provided within the given commandFile. Throw a
     * CommandProcessorException on error.
     *
     * @param commandFile
     */
    public static void processCommandFile(String commandFile) {
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
                    processCommand(line, lineNumber);
                }
                lineNumber++;
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * helper function, check process-transaction command for missing items and
     * report it otherwise, process transaction
     */
    public static String procTransHelp(List<String> a, int lineNumber) {
        String action = a.get(0);
        // id
        String id;
        id = a.get(1);
        // amount tag
        if (!a.get(2).equals("amount")) {
            System.out.println(new CommandProcessorException(action, "amount missing", lineNumber));
        }
        // amount value
        int amount = Integer.parseInt(a.get(3));
        // fee tag
        if (!a.get(4).equals("fee")) {
            System.out.println(new CommandProcessorException(action, "fee missing", lineNumber));
        }
        int fee = Integer.parseInt(a.get(5));
        // note tag
        if (!a.get(6).equals("note")) {
            System.out.println(new CommandProcessorException(action, "note missing", lineNumber));
        }
        // note text
        String note = a.get(7);
        if (note.length() > 1024) {
            System.out.println(
                    new CommandProcessorException(action, "note must be less than 1024 characters", lineNumber));
        }
        // payer tag
        if (!a.get(8).equals("payer")) {
            System.out.println(a.get(8));
            System.out.println(new CommandProcessorException(action, "payer missing", lineNumber));
        }
        // payer account
        Account payer = null;
        try {
            payer = ledger.getBalancePool().get(a.get(9));
        } catch (Exception e) {
            System.out.println(new CommandProcessorException(action, "payer not found", lineNumber));
        }
        // receiver tag
        if (!a.get(10).equals("receiver")) {
            System.out.println(new CommandProcessorException(action, "receiver missing", lineNumber));
        }
        // receiver account
        Account receiver = null;
        try {
            receiver = ledger.getBalancePool().get(a.get(11));
        } catch (Exception e) {
            System.out.println(new CommandProcessorException(action, "receiver not found", lineNumber));
        }
        // command should be ok, process transaction
        String transId = "-1";
        try {
            transId = ledger.processTransaction(new Transaction(id, amount, fee, note, payer, receiver));
        } catch (LedgerException e) {
            System.out.println(new CommandProcessorException(e.action, e.reason, lineNumber));
        }

        return transId;
    }
}
