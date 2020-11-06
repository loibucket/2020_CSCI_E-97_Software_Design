package cscie97.smartcity.authenticator;

public class Permission extends AuthElement {

    public Permission(String id, String name, String description) {
        super(id, name, description, AuthElemType.Permission);
    }

    @Override
    public void acceptVisitor(Visitor v) {

    }

}
