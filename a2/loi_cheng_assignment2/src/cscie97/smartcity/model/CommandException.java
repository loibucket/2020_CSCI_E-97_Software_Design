package cscie97.smartcity.model;

/**
 * Exception is used when input command generates an error
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class CommandException extends Exception{

	public String command;
	public String reason;
	public Integer lineNumber;

	// Create an exception without any parameters provided
	public CommandException() {
		this.command = "unspecified";
		this.reason = "unspecified";
		this.lineNumber = -1;
	}

	// Create an exception with only the command provided
	public CommandException(String command) {
		this.command = command;
		this.reason = "unspecified";
		this.lineNumber = -1;
	}

	// Create an exception with command and reason
	public CommandException(String command, String reason) {
		this.command = command;
		this.reason = reason;
		this.lineNumber = -1;
	}

	// Create an exception with command, reason and line number
	public CommandException(String command, String reason, Integer lineNumber) {
		this.command = command;
		this.reason = reason;
		this.lineNumber = lineNumber;
	}

	public String toString() {
		return ("ERROR: line " + this.lineNumber + ", COMMAND: " + this.command + ", REASON: " + this.reason);
	}

}
