package cscie97.smartcity.authenticator;

import java.security.NoSuchAlgorithmException;

public class Login extends Credential {

    public Login(String username, String password) throws NoSuchAlgorithmException {
        super(username, password);
    }

}
