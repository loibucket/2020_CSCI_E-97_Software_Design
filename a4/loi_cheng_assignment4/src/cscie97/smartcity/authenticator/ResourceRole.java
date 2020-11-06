package cscie97.smartcity.authenticator;

import java.util.List;

public class ResourceRole extends AuthElement {
    
    public ResourceRole(String id, String name, String description) {
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
