package cscie97.smartcity.authenticator;

public class Root extends AuthElement {
    public Root(String id, String name, String description) {
        super(id, name, description, AuthElemType.Root);
    }
}
