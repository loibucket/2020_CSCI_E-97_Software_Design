package cscie97.smartcity.authenticator;

import java.util.List;

public class User extends AuthElement {

    private Login login;
    private FacePrint facePrint;
    private VoicePrint voicePrint;
    private AuthToken authToken;

    public Login getLogin() {
        return login;
    }

    public FacePrint getFacePrint() {
        return facePrint;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public VoicePrint getVoicePrint() {
        return voicePrint;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public void setFacePrint(FacePrint facePrint) {
        this.facePrint = facePrint;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public void setVoicePrint(VoicePrint voicePrint) {
        this.voicePrint = voicePrint;
    }

    public User(String id, String name, String description) {
        super(id, name, description, AuthElemType.User);
    }

    @Override
    public void acceptVisitor(Visitor v) {

    }
}
