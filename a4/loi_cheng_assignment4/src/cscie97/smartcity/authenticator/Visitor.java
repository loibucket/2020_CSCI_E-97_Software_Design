package cscie97.smartcity.authenticator;

import java.util.Map;

/**
 * The visitor travels through the composite and reads data in the entitlements
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-11-15
 */
public interface Visitor {

    /**
     * General visitor interface
     *
     * @param elem     entitlement being visited
     * @param id       id of entitlement
     * @param name     name
     * @param desc     description
     * @param subAuths sub entitlements
     * @throws AuthException on error
     */
    public void getState(String cityId, EntType elem, String id, String name, String desc, Map<String, Entitlement> subAuths) throws AuthException;

    public Object outcome();

    public Boolean visitComplete();

}
