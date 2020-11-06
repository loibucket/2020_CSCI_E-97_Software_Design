package cscie97.smartcity.authenticator;

import java.util.Map;

public abstract class AuthElement {

    private String id;
    private String name;
    private String description;
    private AuthElemType elem;
    private Map<String, AuthElement> subAuths;

    public AuthElement(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.elem = null;
    }

    public AuthElement(String id, String name, String description, AuthElemType elem) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.elem = elem;
    }

    public void acceptVisitor(Visitor v) throws AccessException {
        v.getState(elem, id, name, description, subAuths);
    }

    public void addSubAuth(String id, AuthElement a) {
        subAuths.put(id, a);
    }

    public Map<String, AuthElement> getSubAuths() {
        return subAuths;
    }

    public AuthElemType getElem() {
        return elem;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
