package cscie97.smartcity.authenticator;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all tasks related to authentication
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-11-15
 */
public class Authenticator {

    // admin city is the last access city before bootstrap is off
    private static AccessCity adminCity;

    // start with bypass authentication
    private static Boolean bootStrapMode = true;

    // keep record of all entitlements and city access
    private static final Map<String, Entitlement> entMap = new HashMap<>();
    private static final Map<String, AccessCity> accessCityMap = new HashMap<>();

    // these hold the current token in use, and the current city under access
    private static AuthToken token;
    private static AccessCity accessToCity;

    //getter
    public static AuthToken getToken() {
        return token;
    }

    //getter
    public static AccessCity getAccessToCity() {
        return accessToCity;
    }

    //while in bootstrap mode, bypass authentication
    public static Boolean getBootStrapMode() {
        return bootStrapMode;
    }

    //end boostrap mode
    public static void endBootStrapMode() {
        if (bootStrapMode) {
            adminCity = accessToCity;
            bootStrapMode = false;
        }
    }

    /**
     * logins a user by putting a valid token in use
     * given a list of string with a user and pass (password, faceprint, or voiceprint)
     *
     * @param a the string
     * @throws AuthException on werror
     */
    public static void login(List<String> a) throws AuthException {
        //expire the old token
        if (token != null) {
            token.expire();
            System.out.println(token);
        }

        //find user in authenticator
        if (a.size() < 2) {
            throw new AuthException("login", "error!");
        }
        if (a.size() == 2) {
            Authenticator.token = Authenticator.requestToken(a.get(1), null);
        } else {
            Authenticator.token = Authenticator.requestToken(a.get(1), a.get(2));
        }

        //show new token
        System.out.println(token);
        showEntitlements(a.get(1));
    }


    /**
     * request a specific token for a user
     *
     * @param userId   the user id
     * @param password pass if provided, correct pass renews token
     * @return the token
     * @throws AuthException if error
     */
    private static AuthToken requestToken(String userId, String password) throws AuthException {

        // user access check
        User user = (User) accessToCity.getSubAuths().get(userId);

        // admin access check
        if (user == null) {
            user = (User) adminCity.getSubAuths().get(userId);
        }

        if (user == null) {
            throw new AuthException("authenticator", "user not found!");
        }
        if (password == null) {
            return user.getToken();
        }
        return user.getCreds().updateToken(userId, password);
    }

    /**
     * authenticate a user
     * throws error, halts rest of code if fail to authenticate
     *
     * @param method     the name of method
     * @param identifier an id or name of an entity
     * @throws AuthException error
     */
    public static void authenticate(String method, String identifier) throws AuthException {
        System.out.println("  AUTHENTICATING: " + method + " " + identifier);
        if (bootStrapMode) {
            return; // skip authentication during boostrap
        }

        token.updateTime();

        //has token expired
        if (token.getState() == TokenState.expired) {
            throw new AuthException(method, "expired token!");
        }

        //check permission access and resource role access, either one is fine
        if (!Authenticator.checkAccess(EntType.Permission, null, method, null) // The permission applies to all resources in city
                && !Authenticator.checkAccess(EntType.ResourceRole, null, method, identifier)) { // The resource role applies only to child resources
            throw new AuthException(method, "access error!");
        }

        //marked token used
        token.tokenUsed();
    }

    /**
     * Check access of a user
     *
     * @param elem       the type of entitlement to check
     * @param id         the id
     * @param name       name of entitlement
     * @param resourceId resource id if available
     * @return true if found false if not
     * @throws AuthException on some error
     */
    private static Boolean checkAccess(EntType elem, String id, String name, String resourceId) throws AuthException {
        // local city user access check
        Boolean userAccess;
        User user = (User) accessToCity.getSubAuths().get(token.getUserId());
        if (user != null) {
            FindEntitlementVisitor v = new FindEntitlementVisitor(elem, id, name, resourceId, false);
            user.acceptVisitor(v);
            userAccess = (Boolean) v.outcome();
            if (userAccess) {
                return true;
            }
        }

        // admin access check
        Boolean adminAccess = false;
        User admin = (User) adminCity.getSubAuths().get(token.getUserId());
        if (admin != null) {
            FindEntitlementVisitor w = new FindEntitlementVisitor(elem, id, name, resourceId, false);
            admin.acceptVisitor(w);
            adminAccess = (Boolean) w.outcome();
        }
        return adminAccess;
    }

    /**
     * logout by marking token as expired
     */
    public static void logout() {
        token.expire();
        System.out.println(token);
    }

    /**
     * change the current city
     *
     * @param cityId the city id
     * @throws AuthException if city not found
     */
    public static void setCityAccess(String cityId) throws AuthException {
        try {
            accessToCity = accessCityMap.get(cityId);
        } catch (Exception e) {
            throw new AuthException("setCityAccess", "city access key not found!");
        }
    }

    /**
     * define a permission to run a specific method
     *
     * @param permissionId          unique id
     * @param permissionName        name of permission
     * @param permissionDescription optional description
     */
    public static void definePermission(String permissionId, String permissionName, String permissionDescription) throws AuthException {
        Authenticator.authenticate("definePermission", permissionId);

        Permission p = new Permission(accessToCity.getId(), permissionId, permissionName, permissionDescription);
        entMap.put(accessToCity.getId() + ":" + permissionId, p);
    }

    /**
     * define a role to group permissions or other things
     *
     * @param roleId          unique id
     * @param roleName        name
     * @param roleDescription optional description
     */
    public static void defineRole(String roleId, String roleName, String roleDescription) throws AuthException {
        Authenticator.authenticate("defineRole", roleId);

        Role r = new Role(accessToCity.getId(), roleId, roleName, roleDescription);
        entMap.put(accessToCity.getId() + ":" + roleId, r);
    }


    /**
     * Create a city access entitlement, at root of tree
     *
     * @param cityId   id of city
     * @param cityName name
     */
    public static void createAccessToCity(String cityId, String cityName) throws AuthException {
        Authenticator.authenticate("createCityAccess", cityId);

        AccessCity city = new AccessCity(cityId, cityName);
        accessCityMap.put(cityId, city);
        entMap.put(cityId, city);
        setCityAccess(cityId);
    }

    /**
     * Creates users as part of city
     *
     * @param userId   user id
     * @param userName name
     * @throws NoSuchAlgorithmException on error
     */
    public static void createUser(String userId, String userName) throws NoSuchAlgorithmException, AuthException {
        Authenticator.authenticate("createUser", userId);

        // authentication users are tied to each city
        // names are prefixed by city to be unique for each city
        User user = new User(accessToCity.getId(), userId, userName);
        accessToCity.addSubAuth(userId, user);
        entMap.put(accessToCity.getId() + ":" + userId, user);
    }

    /**
     * Creates a generic resource
     *
     * @param resourceId  id
     * @param resource    the name of the resource
     * @param Description optional description
     */
    public static void createResource(String resourceId, String resource, String Description) throws AuthException {
        Authenticator.authenticate("createResource", resourceId);

        Resource r = new Resource(accessToCity.getId(), resourceId, resource, Description);
        entMap.put(accessToCity.getId() + ":" + resourceId, r);
    }

    /**
     * Creates a resource role, which provides very specific access to resource
     *
     * @param rrId          id
     * @param rrName        name
     * @param rrDescription description
     * @throws AuthException error
     */
    public static void createResourceRole(String rrId, String rrName, String rrDescription) throws AuthException {
        Authenticator.authenticate("createResourceRole", rrId);
        ResourceRole rr = new ResourceRole(accessToCity.getId(), rrId, rrName, rrDescription);
        entMap.put(accessToCity.getId() + ":" + rrId, rr);
    }

    /**
     * add sub entitlements to the parent
     *
     * @param parentEnt the parent entitlement
     * @param childEnt  the child
     * @throws AuthException error
     */
    public static void addSub(String parentEnt, String childEnt) throws AuthException {
        Authenticator.authenticate("addSub", parentEnt);

        Entitlement p = entMap.get(accessToCity.getId() + ":" + parentEnt);
        if (p == null) {
            throw new AuthException("authenticator", parentEnt + " not found!");
        }
        Entitlement c = entMap.get(accessToCity.getId() + ":" + childEnt);
        if (c == null) {
            throw new AuthException("authenticator", accessToCity.getId() + ":" + childEnt + " not found!");
        }
        p.addSubAuth(c.getId(), c);
    }

    /**
     * add a user credential, either a password, voiceprint or faceprint
     *
     * @param userId         the user to add
     * @param credentialType the type of credential
     * @param value          the value itself
     * @throws AuthException on error
     */
    public static void addUserCredential(String userId, String credentialType, String value) throws AuthException {
        Authenticator.authenticate("addSub", userId);

        User user = (User) entMap.get(accessToCity.getId() + ":" + userId);
        if (user == null) {
            throw new AuthException("authenticator", accessToCity.getId() + ":" + ":" + userId + " not found!");
        }
        if (!user.getClass().toString().equals("class cscie97.smartcity.authenticator.User")) {
            throw new AuthException("authenticator", accessToCity.getId() + ":" + ":" + userId + " not a user!");
        }
        if (credentialType.equals("password")) {
            user.getCreds().setPass(null, value);
        } else if (credentialType.equals("biometric")) {
            List<String> a = Arrays.asList(value.split(":"));
            if (a.get(0).equals("voiceprint")) {
                user.getCreds().setVoice(null, value);
            } else if (a.get(0).equals("faceprint")) {
                user.getCreds().setFace(null, value);
            } else {
                throw new AuthException("authenticator", "set biometric error");
            }
        } else {
            throw new AuthException("authenticator", "set credential error");
        }
    }

    /**
     * Shows all the entitlements for a user
     * Traverses the entire user tree
     *
     * @param userId user id
     * @throws AuthException on error
     */
    public static void showEntitlements(String userId) throws AuthException {
        // find if user has general access role or specific resource role
        User user;
        try {
            user = (User) accessToCity.getSubAuths().get(userId);
            if (user == null) {
                user = (User) adminCity.getSubAuths().get(userId);
            }

            // System.out.println(user.toString());
            // user visitor as required
            Visitor v = new FindEntitlementVisitor(null, null, null, null, true);
            user.acceptVisitor(v);

        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthException("showEntitlements", "user not found!");
        }
    }

    /**
     * Shows all the entitlements for a user
     * Traverses the entire user tree
     *
     * @throws AuthException on error
     */
    public static void showAllEntitlements() throws AuthException {
        // find if user has general access role or specific resource role
        for (AccessCity city : accessCityMap.values()) {
            for (Entitlement user : city.getSubAuths().values()) {
                Visitor v = new FindEntitlementVisitor(null, null, null, null, true);
                user.acceptVisitor(v);
                System.out.println(" ");
            }
        }
    }
}
