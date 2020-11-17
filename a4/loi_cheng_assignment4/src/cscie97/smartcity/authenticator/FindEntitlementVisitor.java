package cscie97.smartcity.authenticator;

import java.util.Map;

/**
 * The visitor traverses the trees to find the properties of the entitlements associated with a user
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-11-15
 */
public class FindEntitlementVisitor implements Visitor {

    private final EntType findElem;
    private final String findName;
    private final String findId;
    private final String findResourceName;
    private Boolean complete;
    private Boolean fullVisit;

    /**
     * Go and find a specific entitlement
     *
     * @param elem      the type of entitlement
     * @param idA       first identifier, usually id
     * @param idB       second identifier, name
     * @param idC       third identifier, optional, for ResourceRole this is the resourceId
     * @param fullVisit false to stop once found, true to traverse the entire tree
     */
    public FindEntitlementVisitor(EntType elem, String idA, String idB, String idC, Boolean fullVisit) {
        this.findElem = elem;
        this.findName = idB;
        this.complete = false;
        this.findId = idA;
        this.fullVisit = fullVisit;
        this.findResourceName = idC;
        //System.out.println("VISITOR FIND: elem: " + elem + " name: " + name);
    }

    /**
     * Called by the entitlements to pass in the info for the visitor
     *
     * @param elem     the element type visited
     * @param id       id
     * @param name     name
     * @param desc     description (or resource id)
     * @param subAuths sub entitlements
     */
    @Override
    public void getState(String cityId, EntType elem, String id, String name, String desc, Map<String, Entitlement> subAuths) {

        if (fullVisit) {
            System.out.println("  ENTITLEMENT AT: city: " + cityId + " elem: " + elem + " id: " + id + " name: " + name + " desc: " + desc + " subauths: " + subAuths.size());
        }

        // if not looking for resource role
        if (this.findResourceName == null) {
            if (elem == findElem && name.equals(this.findName)) {
                if (!fullVisit) {
                    this.complete = true;
                }
            }
            // looking for resource role
        } else if (elem == EntType.ResourceRole) {
            if (name.equals(findName)) {
                for (Entitlement sub : subAuths.values()) {
                    if (sub.getName().equals(findResourceName)) {
                        this.complete = true;
                        break;
                    }
                }
            }
        }
    }

    /**
     * check if the element was found
     */
    @Override
    public Object outcome() {
        return complete;
    }

    /**
     * check if time to go home
     *
     * @return true if time to go home
     */
    public Boolean visitComplete() {
        return complete;
    }

}