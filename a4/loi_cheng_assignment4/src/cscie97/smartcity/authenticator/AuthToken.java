package cscie97.smartcity.authenticator;

import java.util.Date;

public class AuthToken {

    private String id;

    private Date lastUsed;

    private Enum state;

    public AuthToken(String id) {
        this.id = id;
        lastUsed = null;
        state = TokenState.expired;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "id='" + id + '\'' +
                ", lastUsed=" + lastUsed +
                ", state=" + state +
                '}';
    }

    public String getId() {
        return id;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public Enum getState() {
        return state;
    }
}
