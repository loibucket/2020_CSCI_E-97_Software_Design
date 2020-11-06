package cscie97.smartcity.authenticator;

import java.util.Map;

public class findElementVisitor implements Visitor {

    private AuthElemType findElem;
    private String findId;
    private String findName;
    private String findDescription;

    private boolean found;

    public findElementVisitor(AuthElemType elem, String id, String name, String desc) {
        this.findElem = elem;
        this.findId = id;
        this.findName = name;
        this.findDescription = desc;
        this.found = false;
    }

    @Override
    public void getState(AuthElemType elem, String id, String name, String desc, Map<String, AuthElement> subAuths) {
        if (elem == findElem) {

        }
    }

    public boolean elementFound() {
        return found;
    }

}