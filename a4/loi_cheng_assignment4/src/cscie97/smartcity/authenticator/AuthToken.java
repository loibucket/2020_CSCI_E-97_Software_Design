package cscie97.smartcity.authenticator;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Auth Token
 * Each auth token can only be used for one session
 * Once marked expired, it cannot be reinstated, user needs to get another new token
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class AuthToken {

    private final int useLimit = 100; //token can only be used 20 times in authentication
    private final String userId;
    private Timestamp lastUsed;
    private Enum state;
    private UUID uuid;
    private int counter;

    /**
     * Constructor
     *
     * @param userId the userId
     */
    public AuthToken(String userId) {
        this.userId = userId;
        this.lastUsed = new Timestamp(System.currentTimeMillis());
        this.state = TokenState.active;
        this.uuid = UUID.randomUUID();
        this.counter = 0;
    }

    /**
     * update the token
     */
    public void updateTime() {
        this.lastUsed = new Timestamp(System.currentTimeMillis());
    }

    /**
     * expire the token
     */
    public void expire() {
        this.state = TokenState.expired;
    }

    /**
     * get user id of token
     *
     * @return user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * get then unique id of token
     *
     * @return uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * token was used, increment the counter
     */
    public void tokenUsed() {
        this.counter++;
        if (this.counter > useLimit) {
            expire();
        }
    }

    //getter
    public Date getLastUsed() {
        return lastUsed;
    }

    //getter
    public Enum getState() {
        return state;
    }

    //to String

    @Override
    public String toString() {
        return "AuthToken{" +
                "userId='" + userId + '\'' +
                ", lastUsed=" + lastUsed +
                ", state=" + state +
                ", uuid=" + uuid +
                '}';
    }
}
