package cscie97.smartcity.model;

public class CommandAPIException extends Exception{

	public String command;

	public String reason;

	public int lineNumber;

	// Create an exception without any parameters provided
	public CommandAPIException() {
		this.command = "unspecified";
		this.reason = "unspecified";
		this.lineNumber = -1;
	}

	// Create an exception with only the command provided
	public CommandAPIException(String command) {
		this.command = command;
		this.reason = "unspecified";
		this.lineNumber = -1;
	}

	// Create an exception with commmand and reason
	public CommandAPIException(String command, String reason) {
		this.command = command;
		this.reason = reason;
		this.lineNumber = -1;
	}

	// Create an execption with comand, reason and line number
	public CommandAPIException(String command, String reason, int lineNumber) {
		this.command = command;
		this.reason = reason;
		this.lineNumber = lineNumber;
	}

	public String toString() {
		return ("ERROR: line " + this.lineNumber + ", COMMAND: " + this.command + ", REASON: " + this.reason);
	}

}
