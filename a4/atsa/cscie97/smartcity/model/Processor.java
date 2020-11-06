package cscie97.smartcity.model;

import cscie97.smartcity.authenticator.AuthToken;

public interface Processor {

	public abstract void processCommand(AuthToken token, String command);

	public abstract void processCommandFile(AuthToken token, String file);

}
