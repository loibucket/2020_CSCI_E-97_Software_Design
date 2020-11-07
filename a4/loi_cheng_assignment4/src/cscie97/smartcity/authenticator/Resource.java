package cscie97.smartcity.authenticator;

public class Resource extends AuthElement {

    public Resource(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public void acceptVisitor(Visitor v) {

    }

}
