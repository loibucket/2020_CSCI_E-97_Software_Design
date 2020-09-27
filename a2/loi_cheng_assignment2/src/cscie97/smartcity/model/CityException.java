package cscie97.smartcity.model;

public class CityException extends Exception{

	public String action;

	public String reason;

	// Create an exception without any parameters provided
	public CityException() {
		this.action = "unspecified";
		this.reason = "unspecified";
	}

	// Create an exception with action provided
	public CityException(String action) {
		this.action = action;
		this.reason = "unspecified";
	}

	// Create an exception with action and reason provided
	public CityException(String action, String reason) {
		this.action = action;
		this.reason = reason;
	}

	public String toString() {
		return ("ERROR: " + ", ACTION: " + this.action + ", REASON: " + this.reason);
	}

}
