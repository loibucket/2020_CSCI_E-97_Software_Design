package cscie97.smartcity.authenticator;

import java.security.NoSuchAlgorithmException;

/**
 * The user at the root of the tree holds multiple entitlement items
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-11-15
 */
public class User extends Entitlement {

    private final Credential creds;

    /**
     * User entitlement
     *
     * @param userId id
     * @param name   name
     * @throws NoSuchAlgorithmException
     */
    public User(String cityId, String userId, String name) throws NoSuchAlgorithmException {
        super(cityId, userId, name, "Smart City User", EntType.User);
        this.creds = new Credential(userId);
    }

    public Credential getCreds() {
        return creds;
    }

    public AuthToken getToken() {
        return creds.getUserToken();
    }

}