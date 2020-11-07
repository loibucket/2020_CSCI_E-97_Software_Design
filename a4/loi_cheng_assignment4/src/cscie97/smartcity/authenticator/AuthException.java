package cscie97.smartcity.authenticator;

public class AuthException extends Throwable {

    public String action;
    public String reason;

    // Create an exception without any parameters provided
    public AuthException() {
        this.action = "unspecified";
        this.reason = "unspecified";
    }

    // Create an exception with action provided
    public AuthException(String action) {
        this.action = action;
        this.reason = "unspecified";
    }

    // Create an exception with action and reason provided
    public AuthException(String action, String reason) {
        this.action = action;
        this.reason = reason;
    }

    public String toString() {
        return ("AUTH ERROR: " + ", ACTION: " + this.action + ", REASON: " + this.reason);
    }

}
