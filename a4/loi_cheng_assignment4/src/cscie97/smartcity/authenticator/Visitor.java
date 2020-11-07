package cscie97.smartcity.authenticator;

import java.util.Map;

public interface Visitor {

    public void getState(AuthElemType elem, String id, String name, String desc, Map<String, AuthElement> subAuths) throws AuthException;

}
