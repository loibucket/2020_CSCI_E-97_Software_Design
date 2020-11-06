package cscie97.smartcity.authenticator;

public class AccessException extends Throwable {

    public String action;
    public String reason;

    // Create an exception without any parameters provided
    public AccessException() {
        this.action = "unspecified";
        this.reason = "unspecified";
    }

    // Create an exception with action provided
    public AccessException(String action) {
        this.action = action;
        this.reason = "unspecified";
    }

    // Create an exception with action and reason provided
    public AccessException(String action, String reason) {
        this.action = action;
        this.reason = reason;
    }

    public String toString() {
        return ("ACCESS ERROR: " + ", ACTION: " + this.action + ", REASON: " + this.reason);
    }

}
