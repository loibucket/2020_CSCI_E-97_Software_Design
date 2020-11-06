package cscie97.smartcity.authenticator;

import java.util.List;

public class Resource extends AuthElement {
	
    public Resource(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public void acceptVisitor(Visitor v) {

    }

    @Override
    public List<AuthElement> getAuths() {
        return null;
    }
}
