package cscie97.smartcity.authenticator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Stores the password, face print and voice print of user
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-11-15
 */
public class Credential {

    private final String userId;
    private byte[] passHash;
    private byte[] faceHash;
    private byte[] voiceHash;
    private final MessageDigest digest;
    private AuthToken userToken;

    /**
     * create a credential store for user, set up the hash
     *
     * @param userId user id
     * @throws NoSuchAlgorithmException
     */
    public Credential(String userId) throws NoSuchAlgorithmException {
        this.userId = userId;
        digest = MessageDigest.getInstance("SHA-256");
    }

    /**
     * hash and store password
     *
     * @param oldPass old password, null if first time
     * @param newPass new password
     * @throws AuthException some error
     */
    public void setPass(String oldPass, String newPass) throws AuthException {
        if (passHash == null || digest.digest(oldPass.getBytes(StandardCharsets.UTF_8)) == passHash) {
            passHash = digest.digest(newPass.getBytes(StandardCharsets.UTF_8));
        } else {
            throw new AuthException("set credential", "passwords do not match!");
        }
    }

    /**
     * face print
     *
     * @param oldPass old, null if first time
     * @param newPass new
     * @throws AuthException some error
     */
    public void setFace(String oldPass, String newPass) throws AuthException {
        if (faceHash == null || digest.digest(oldPass.getBytes(StandardCharsets.UTF_8)) == faceHash) {
            faceHash = digest.digest(newPass.getBytes(StandardCharsets.UTF_8));
        } else {
            throw new AuthException("set credential", "faceprints do not match!");
        }
    }

    /**
     * voice print
     *
     * @param oldPass old, null if first time
     * @param newPass new
     * @throws AuthException some error
     */
    public void setVoice(String oldPass, String newPass) throws AuthException {
        if (voiceHash == null || digest.digest(oldPass.getBytes(StandardCharsets.UTF_8)) == voiceHash) {
            voiceHash = digest.digest(newPass.getBytes(StandardCharsets.UTF_8));
        } else {
            throw new AuthException("set credential", "voiceprints do not match!");
        }
    }

    /**
     * update the token
     *
     * @param user user id
     * @param pass any of the 3 pass
     * @return a valid token
     * @throws AuthException error
     */
    public AuthToken updateToken(String user, String pass) throws AuthException {
        byte[] passIn = digest.digest(pass.getBytes(StandardCharsets.UTF_8));
        if (Arrays.equals(passHash, passIn) || Arrays.equals(faceHash, passIn) || Arrays.equals(voiceHash, passIn)) {
            if (this.userToken == null || this.userToken.getState() == TokenState.expired) {
                this.userToken = new AuthToken(user);
            }
            return this.userToken;
        } else {
            throw new AuthException("credential", "credentials not valid!");
        }
    }

    /**
     * get token attached to user
     *
     * @return token
     */
    public AuthToken getUserToken() {
        return this.userToken;
    }

    //getter
    public String getUserId() {
        return userId;
    }
}
