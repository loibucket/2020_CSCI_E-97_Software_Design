package cscie97.smartcity.authenticator;

public class Role extends AuthElement {

    public Role(String id, String name, String description) {
        super(id, name, description, AuthElemType.Role);
    }

    @Override
    public void acceptVisitor(Visitor v) {

    }

}
