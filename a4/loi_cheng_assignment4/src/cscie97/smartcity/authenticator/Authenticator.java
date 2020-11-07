package cscie97.smartcity.authenticator;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Authenticator {

    //private static final Map<String, Role> roleMap = new HashMap<>();
    //private static final Map<String, Permission> permissionMap = new HashMap<>();
    //private static final Map<String, User> userMap = new HashMap<>();
    //private static final Map<String, ResourceRole> resourceRoleMap = new HashMap<>();
    //private static final Map<String, Resource> resourceMap = new HashMap<>();

    private static final Map<String, AuthElement> authMap = new HashMap<>();
    private static final AuthElement root = new Root("root", "root", "root");

    public static void definePermission(String permissionId, String permissionName, String permissionDescription) {
        Permission p = new Permission(permissionId, permissionName, permissionDescription);
        //permissionMap.put(permissionId, p);
        authMap.put(permissionId, p);
    }

    public static void defineRole(String roleId, String roleName, String roleDescription) {
        Role r = new Role(roleId, roleName, roleDescription);
        //roleMap.put(roleId, r);
        authMap.put(roleId, r);
    }

    public static void addSub(String parentAuth, String childAuth) throws AuthException {

        AuthElement p = authMap.get(parentAuth);
        if (p == null) {
            throw new AuthException("authenticator", parentAuth + " not found!");
        }

        AuthElement c = authMap.get(childAuth);
        if (c == null) {
            throw new AuthException("authenticator", childAuth + " not found!");
        }

        p.addSubAuth(c.getId(), c);
    }

    public static void addUserCredential(String userId, String credentialType, String value) throws NoSuchAlgorithmException, AuthException {
        User user = (User) authMap.get(userId);
        if (user == null) {
            throw new AuthException("authenticator", userId + " not found!");
        }
        if (!user.getClass().toString().equals("class cscie97.smartcity.authenticator.User")) {
            throw new AuthException("authenticator", userId + " not a user!");
        }
        if (credentialType.equals("password")) {
            Login login = new Login(userId, value);
            user.setLogin(login);
        } else if (credentialType.equals("biometric")) {
            List<String> a = Arrays.asList(value.split(":"));
            if (a.get(0).equals("voiceprint")) {
                VoicePrint v = new VoicePrint(userId, value);
            } else if (a.get(0).equals("faceprint")) {
                FacePrint f = new FacePrint(userId, value);
            } else {
                throw new AuthException("authenticator", "set biometric error");
            }
        } else {
            throw new AuthException("authenticator", "set credential error");
        }
    }

    public static void createUser(String userId, String userName) {
        User user = new User(userId, userName, null);
        root.addSubAuth(userId, user);
        authMap.put(userId, user);
        AuthToken token = new AuthToken(userId);
        user.setAuthToken(token);
    }

    public static void createResource(String resourceId, String resource, String Description) {
        Resource r = new Resource(resourceId, resource, Description);
        authMap.put(resourceId, r);
    }

    public static void createResourceRole(String resourceRoleName, String role, String resource) throws AuthException {

        ResourceRole rr = new ResourceRole(resourceRoleName, null, null);

        AuthElement ro = authMap.get(role);
        if (ro == null) {
            throw new AuthException("authenticator", role + " not found!");
        }

        AuthElement re = authMap.get(resource);
        if (re == null) {
            throw new AuthException("authenticator", resource + " not found!");
        }

        ro.addSubAuth(resourceRoleName, rr);
        rr.addSubAuth(resource, authMap.get(resource));
        authMap.put(resourceRoleName, rr);
    }

    public static Boolean checkAccess(AuthToken token) throws AuthException {
        AuthTokenVisitor v = new AuthTokenVisitor(token);
        root.acceptVisitor(v);
        return v.getTokenFound();
    }

    public static AuthToken requestToken(String userId) {
        return null;
    }

    public static void logout() {

    }

}
