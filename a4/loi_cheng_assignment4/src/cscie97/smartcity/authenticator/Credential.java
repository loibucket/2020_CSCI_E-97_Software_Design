package cscie97.smartcity.authenticator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Credential {

    private String username;
    private byte[] passHash;
    private MessageDigest digest;

    public Credential(String username, String password) throws NoSuchAlgorithmException {
        this.username = username;
        digest = MessageDigest.getInstance("SHA-256");
        passHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
    }

    public void changePass(String oldPass, String newPass) {
        if (digest.digest(oldPass.getBytes(StandardCharsets.UTF_8)) == passHash) {
            passHash = digest.digest(newPass.getBytes(StandardCharsets.UTF_8));
        }
    }

    public String getUsername() {
        return username;
    }

    public byte[] getPassHash() {
        return passHash;
    }

}
