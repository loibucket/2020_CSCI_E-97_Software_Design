package cscie97.smartcity.authenticator;

public class AuthToken extends User implements AuthElement {

	private String id;

	private Date lastUsed;

	private Enum state;

}
