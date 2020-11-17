package cscie97.smartcity.authenticator;

import java.util.HashMap;
import java.util.Map;

/**
 * Building blocks of the permission composite tree
 * Each entitlement gives the user a specific privilege
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-11-15
 */
public class Entitlement {

    private final String cityId;
    private final String id;
    private final String name;
    private final String description;
    private final EntType elem;
    private final Map<String, Entitlement> subAuths;

    /**
     * constructor
     *
     * @param id          id
     * @param name        name
     * @param description description
     * @param elem        explicitly defined entitlement type
     */
    public Entitlement(String cityId, String id, String name, String description, EntType elem) {
        this.cityId = cityId;
        this.id = id;
        this.name = name;
        this.description = description;
        this.elem = elem;
        this.subAuths = new HashMap<>();
        System.out.println(toString());
    }

    /**
     * provide the visitor with the info, and then send it to next place to visit, depth first search
     */
    public void acceptVisitor(Visitor v) throws AuthException {
        v.getState(cityId, elem, id, name, description, subAuths);
        if (!v.visitComplete() && this.subAuths.size() != 0) {
            for (Entitlement a : this.subAuths.values()) {
                a.acceptVisitor(v);
            }
        }
    }

    /**
     * add child entitlements
     *
     * @param id id of entitlement
     * @param a  the entitlement object
     */
    public void addSubAuth(String id, Entitlement a) {
        subAuths.put(id, a);
    }

    //getter
    public Map<String, Entitlement> getSubAuths() {
        return subAuths;
    }

    //getter
    public EntType getElem() {
        return elem;
    }
    //getter

    public String getId() {
        return id;
    }
    //getter

    public String getName() {
        return name;
    }

    //getter
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "\nEntitlement{" +
                "cityId='" + cityId + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", elem=" + elem +
                ", subAuths=" + subAuths +
                '}';
    }
}
