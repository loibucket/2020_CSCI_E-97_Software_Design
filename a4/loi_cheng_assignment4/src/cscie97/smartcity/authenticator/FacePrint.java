package cscie97.smartcity.authenticator;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class FacePrint extends Credential {


    public FacePrint(String username, String password) throws NoSuchAlgorithmException {
        super(username, password);
    }
}
