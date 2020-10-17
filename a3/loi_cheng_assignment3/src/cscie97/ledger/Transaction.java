package cscie97.ledger;

import java.io.Serializable;

/**
 * The Transaction class represents a transaction in the Ledger System. A
 * transaction contains a transaction id, an amount, a fee, a note, and
 * references a payer account and a receiver account. The transaction amount is
 * transferred from the payer’s account balance to the receiver’s account
 * balance. The transaction fee is transferred from the payer’s account to the
 * master account. Transactions are aggregated within blocks. Final to prevent
 * modifications like additional methods
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-05
 */

public final class Transaction implements Serializable {

    // serial version uid required to make item serializable
    private static final long serialVersionUID = 1L;
    public final String transactionId;
    public final int amount;
    public final int fee;
    public final String note;
    public final Account payer;
    public final Account receiver;

    public Transaction(String transactionId, int amount, int fee, String note, Account payer, Account receiver) {
        /**
         * Unique identifier for the transaction assigned to the transaction by the
         * Ledger System.
         */
        this.transactionId = transactionId;
        /**
         * The transaction amount to be deducted from the payer account and added to the
         * receiver’s account. The amount must be greater or equal to 0 and less than or
         * equal to max.integer.
         */
        this.amount = amount;
        /**
         * The fee is taken from the payer account and added to the master account. The
         * fee must be greater than or equal to 10.
         */
        this.fee = fee;
        /**
         * An arbitrary string that may be up to 1024 characters in length.
         */
        this.note = note;
        /**
         * The Account issuing the transaction. The amount of the transaction and the
         * transaction fee will be deducted from the payer’s account balance.
         */
        this.payer = payer;
        /**
         * The amount of the transaction will be added to the balance of the receiver
         * account.
         */
        this.receiver = receiver;
    }

    public String toString() {
        // output as string format
        return ("\ntransaction id: " + transactionId + " amount: " + amount + " fee: " + fee + " note: " + note
                + " payer: " + payer.getAddress() + " receiver: " + receiver.getAddress());
    }
}
