package cscie97.smartcity.authenticator;

public class Authenticator {

	private Map<roleId,Role> roleMap;

	private Map<permissionId,Permission> permissionMap;

	private Map<userId,User> userMap;

	private Map<resourceRoleId,ResourceRole> resourceRoleMap;

	private Map<resourceId,Resource> resourceMap;

	private User user;

	private Role role;

	private ResourceRole resourceRole;

	private FacePrint facePrint;

	private VoicePrint voicePrint;

	private AuthToken authToken;

	private Resource resource;

	private Permission permission;

	private Permission permission;

	private Resource resource;

	private AuthElement authElement;

	private Visitor visitor;

	private AuthElement authElement;

	public void definePermission(String permissionId, String permissionName, String permissionDescription) {

	}

	public void defineRole(String roleId, String roleName, String roleDescription) {

	}

	public void createUser(String userId, String userName) {

	}

	public void createResourceRole(String resourceRoleName, Role role, Resource resource) {

	}

	public void createResource(String resourceId, String resouceDescription) {

	}

	public void checkAccess(AuthToken token) {

	}

	public AuthToken requestToken(String userId) {
		return null;
	}

	public void logout() {

	}

}
