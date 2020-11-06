package cscie97.ledger;

import java.io.Serializable;

/**
 * The Account class represents an individual account within the Ledger Service.
 * An account contains an address that provides a unique identity for the
 * Account. The Account also contains a balance that represents the value of the
 * account. The account can only be updated by the Ledger Service. Final to
 * prevent modifications like additional methods
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-05
 */
public final class Account implements Serializable {

    // serial version uid required to make item serializable
    private static final long serialVersionUID = 1L;
    /*
     * * Unique identifier for the account, assigned automatically when an Account
     * instance is created.
     */
    private final String address;

    /**
     * Balance of the account which reflects the total transfers to and from the
     * Account, including fees for transactions where the account is the payer.
     */
    private int balance;

    // initialize account with 0 balance
    public Account(String accountId) {
        this.address = accountId;
        this.balance = 0;
    }

    // get address
    public String getAddress() {
        return (this.address);
    }

    // get balance
    public int getBalance() {
        return (this.balance);
    }

    // update balance
    public void updateBalance(int balance) {
        this.balance = balance;
    }

    // to string
    public String toString() {
        return ("\n" + this.address + ": " + this.balance);
    }
}
