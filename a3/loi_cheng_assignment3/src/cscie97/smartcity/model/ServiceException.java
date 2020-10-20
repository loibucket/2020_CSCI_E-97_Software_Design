package cscie97.smartcity.model;

/**
 * Exception on the city, sensors, etc.
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class ServiceException extends Exception {

    public String action;
    public String reason;

    // Create an exception without any parameters provided
    public ServiceException() {
        this.action = "unspecified";
        this.reason = "unspecified";
    }

    // Create an exception with action provided
    public ServiceException(String action) {
        this.action = action;
        this.reason = "unspecified";
    }

    // Create an exception with action and reason provided
    public ServiceException(String action, String reason) {
        this.action = action;
        this.reason = reason;
    }

    public String toString() {
        return ("SERVICE ERROR: " + ", ACTION: " + this.action + ", REASON: " + this.reason);
    }

}
