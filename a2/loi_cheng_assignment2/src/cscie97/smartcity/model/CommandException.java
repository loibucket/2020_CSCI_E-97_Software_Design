package cscie97.smartcity.model;

/**
 * Exception is used when input command generates an error
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class CommandException extends Exception {

    public String command;
    public String action;
    public String reason;
    public Integer lineNumber;

    // Create an exception without any parameters provided
    public CommandException() {
        this.command = "unspecified";
        this.action = "unspecified";
        this.reason = "unspecified";
        this.lineNumber = -1;
    }

    // Create an exception with command command action reason line number
    public CommandException(String command, String action, String reason, Integer lineNumber) {
        this.command = command;
        this.action = action;
        this.reason = reason;
        this.lineNumber = lineNumber;
    }

    public String toString() {
        String line = this.lineNumber == null ? "" : "ERROR: line " + this.lineNumber;
        String command = this.command == null ? "" : ", COMMAND: " + this.command;
        String action = this.action == null ? "" : ", ACTION: " + this.action;
        String reason = this.reason == null ? "" : ", REASON: " + this.reason;
        return (line + action + reason);
    }

}
