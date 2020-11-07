package cscie97.smartcity.authenticator;

import java.util.Map;

public class AuthTokenVisitor implements Visitor {

    private final AuthToken tokenSearch;

    private Boolean tokenFound = false;

    public AuthTokenVisitor(AuthToken token) {
        this.tokenSearch = token;
    }

    @Override
    public void getState(AuthElemType elem, String id, String name, String desc, Map<String, AuthElement> subAuths) throws AuthException {
        if (elem == AuthElemType.Root) {
            try {
                AuthElement a = subAuths.get(tokenSearch.getId());
                User u = (User) a;
                if (u.getAuthToken().toString().equals(tokenSearch.toString())) {
                    tokenFound = true;
                }
            } catch (Exception e) {
                throw new AuthException("auth token visitor", "auth token not found!");
            }
        }
    }

    public AuthToken getTokenSearch() {
        return tokenSearch;
    }

    public Boolean getTokenFound() {
        return tokenFound;
    }
}
