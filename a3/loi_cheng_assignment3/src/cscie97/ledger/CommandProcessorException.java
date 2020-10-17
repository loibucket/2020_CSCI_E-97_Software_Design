package cscie97.ledger;

import java.io.IOException;

@SuppressWarnings("serial")

/**
 * The CommandProcessorException is returned from the CommandProcessor methods
 * in response to an error condition. The CommandProcessorException captures the
 * command that was attempted and the reason for the failure. In the case where
 * commands are read from a file, the line number of the command should be
 * included in the exception.
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-05
 */
public class CommandProcessorException extends IOException {
    // Command that was performed (e.g., “submit transaction”)
    private String command;

    /**
     * Reason for the exception (e.g. “insufficient funds in the payer account to
     * cover the transaction amount and fee”).
     */
    private String reason;

    // The line number of the command in the input file.
    private int lineNumber;

    // Create an exception without any parameters provided
    public CommandProcessorException() {
        this.command = "unspecified";
        this.reason = "unspecified";
        this.lineNumber = -1;
    }

    // Create an exception with only the command provided
    public CommandProcessorException(String command) {
        this.command = command;
        this.reason = "unspecified";
        this.lineNumber = -1;
    }

    // Create an exception with commmand and reason
    public CommandProcessorException(String command, String reason) {
        this.command = command;
        this.reason = reason;
        this.lineNumber = -1;
    }

    // Create an execption with comand, reason and line number
    public CommandProcessorException(String command, String reason, int lineNumber) {
        this.command = command;
        this.reason = reason;
        this.lineNumber = lineNumber;
    }

    public String toString() {
        return ("ERROR: line " + this.lineNumber + ", COMMAND: " + this.command + ", REASON: " + this.reason);
    }

}
