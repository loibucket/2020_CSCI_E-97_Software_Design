package cscie97.ledger;

import cscie97.smartcity.authenticator.AuthToken;
import cscie97.smartcity.shared.FileProcessor;

import java.io.*;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The CommandProcessor is a utility class for feeding the Ledger a set of
 * operations, using command syntax. The command syntax specification follows:
 * <p>
 * Create Ledger Create a new ledger with the given name, description, and seed
 * value.
 * <p>
 * create-ledger <name> description <description> seed <seed>
 * <p>
 * Create Account Create a new account with the given account id.
 * <p>
 * create-account <account-id>
 * <p>
 * Process Transaction Process a new transaction. Return an error message if the
 * transaction is invalid, otherwise output the transaction id.
 * <p>
 * process-transaction <transaction-id> amount <amount> fee <fee> note <note>
 * payer <account-address> receiver <account-address>
 * <p>
 * Get Account Balance Output the account balance for the given account
 * get-account-balance <account-id>
 * <p>
 * Get Account Balances Output the account balances for the most recent
 * completed block.
 * <p>
 * get-account-balances
 * <p>
 * Get Block Output the details for the given block number.
 * <p>
 * get-block <block-number>
 * <p>
 * Get Transaction Output the details of the given transaction id. Return an
 * error if the transaction was not found in the Ledger.
 * <p>
 * get-transaction <transaction-id>
 * <p>
 * Validate
 * <p>
 * Validate the current state of the blockchain. validate Comment lines are
 * denoted with a # in the first column.
 * <p>
 * validate
 *
 * @author Loi Cheng
 * v1.0 2020-09-05 initial
 * v1.1 2020-10-19 auto-increment transaction id, passed in id is discarded
 * @version 1.1
 * @since 2020-10-19
 */
public class CommandProcessor extends FileProcessor {

    // the ledger, this is static so there can only be 1 ledger
    private static Ledger ledger = null;

    // current transaction id
    private static int transactionId = 0;

    /**
     * Process a single command. The output of the command is formatted and
     * displayed to stdout. Throw a CommandProcessorException on error.
     *
     * @param command    the command
     * @param lineNumber the line number
     * @throws CommandProcessorException if process errors
     */
    public static void processCommand(AuthToken authToken, String command, int lineNumber) throws CommandProcessorException {

        System.out.println("LEDGER-OPEN: " + command);
        System.out.println("LEDGER: ");

        // replace special quotes to normal
        command = command.replace('“', '"');
        command = command.replace('”', '"');

        // split string by whitespace, except when between quotes
        // stackoverflow.com/questions/18893390/splitting-on-comma-outside-quotes
        List<String> a = Arrays.asList(command.split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));

        // do different action with commands
        String action = a.get(0);
        try {
            // use helper function procTransHelp
            switch (action) {
                case "create-ledger" -> ledger = new Ledger(a.get(1), a.get(3), a.get(4));
                case "create-account" -> ledger.createAccount(a.get(1));
                case "process-transaction" -> System.out.println("transaction id: " + procTransHelp(a, lineNumber));
                case "get-account-balance" -> {
                    System.out.println("confirmed:" + ledger.getAccountBalance(a.get(1)));
                    System.out.println("unconfirmed:" + ledger.getUnconfirmedBalance(a.get(1)));
                }
                case "get-account-balances" -> System.out.println(ledger.getAccountBalances());
                case "get-block" -> System.out.println(ledger.getBlock(Integer.parseInt(a.get(1))));
                case "get-transaction" -> System.out.println(ledger.getTransaction(a.get(1)).toString().replace("\n", ""));
                case "Validate" -> {
                    ledger.validate();
                    System.out.println("ledger is valid");
                }
                default -> System.out.println((new CommandProcessorException(action, "no such command", lineNumber).toString()));
            }
        } catch (LedgerException e) {
            // print error message, and continue processing next line
            System.out.println((new CommandProcessorException(e.action, e.reason, lineNumber)).toString());
        } catch (Exception e) {
            System.out.println((new CommandProcessorException(action, "processing error!", lineNumber)).toString());
        }
        System.out.println(":LEDGER-CLOSE");
        System.out.println(" "); //line break
    }

    /**
     * Process a set of commands provided within the given commandFile. Throw a
     * CommandProcessorException on error.
     * java
     *
     * @param commandFile the filename
     */
    public static void processCommandFile(AuthToken authToken, String commandFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(commandFile));
            String line;
            AtomicInteger lineNumber = new AtomicInteger(1);
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
            System.out.println((new CommandProcessorException(action, "amount missing", lineNumber)).toString());
        }
        // amount value
        int amount = Integer.parseInt(a.get(3));
        // fee tag
        if (!a.get(4).equals("fee")) {
            System.out.println((new CommandProcessorException(action, "fee missing", lineNumber)).toString());
        }
        int fee = Integer.parseInt(a.get(5));
        // note tag
        if (!a.get(6).equals("note")) {
            System.out.println((new CommandProcessorException(action, "note missing", lineNumber)).toString());
        }
        // note text
        String note = a.get(7);
        if (note.length() > 1024) {
            System.out.println(
                    (new CommandProcessorException(action, "note must be less than 1024 characters", lineNumber)).toString());
        }
        // payer tag
        if (!a.get(8).equals("payer")) {
            System.out.println(a.get(8));
            System.out.println((new CommandProcessorException(action, "payer missing", lineNumber)).toString());
        }
        // payer account
        Account payer = null;
        try {
            payer = ledger.getBalancePool().get(a.get(9));
        } catch (Exception e) {
            System.out.println((new CommandProcessorException(action, "payer not found", lineNumber)).toString());
        }
        // receiver tag
        if (!a.get(10).equals("receiver")) {
            System.out.println((new CommandProcessorException(action, "receiver missing", lineNumber)).toString());
        }
        // receiver account
        Account receiver = null;
        try {
            receiver = ledger.getBalancePool().get(a.get(11));
        } catch (Exception e) {
            System.out.println((new CommandProcessorException(action, "receiver not found", lineNumber)).toString());
        }
        // command should be ok, process transaction
        String transId = "-1";
        try {
            //auto increment transactions
            transactionId++;
            transId = ledger.processTransaction(new Transaction(Integer.toString(transactionId), amount, fee, note, payer, receiver));
        } catch (LedgerException e) {
            System.out.println((new CommandProcessorException(e.action, e.reason, lineNumber)).toString());
        }

        return transId;
    }

}
