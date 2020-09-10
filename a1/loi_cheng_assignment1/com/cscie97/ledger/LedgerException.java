package com.cscie97.ledger;

@SuppressWarnings("serial")

/**
 * The Ledger Exception is returned from the Ledger API methods in response to
 * an error condition. The Ledger Exception captures the action that was
 * attempted and the reason for the failure.
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-05
 */
public class LedgerException extends Exception {

    // Action that was performed (e.g., “submit transaction”)
    public String action;
    /**
     * Reason for the exception (e.g. “insufficient funds in the payer account to
     * cover the transaction amount and fee”).
     */
    public String reason;

    // Create an exception without any parameters provided
    public LedgerException() {
        this.action = "unspecified";
        this.reason = "unspecified";
    }

    // Create an exception with action provided
    public LedgerException(String action) {
        this.action = action;
        this.reason = "unspecified";
    }

    // Create an exception with action and reason provided
    public LedgerException(String action, String reason) {
        this.action = action;
        this.reason = reason;
    }

    public String toString() {
        return ("ERROR: " + ", ACTION: " + this.action + ", REASON: " + this.reason);
    }
}
