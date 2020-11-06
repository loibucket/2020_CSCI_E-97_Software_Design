package cscie97.smartcity.authenticator;

import java.security.NoSuchAlgorithmException;

public class VoicePrint extends Credential {

    public VoicePrint(String username, String password) throws NoSuchAlgorithmException {
        super(username, password);
    }
}
