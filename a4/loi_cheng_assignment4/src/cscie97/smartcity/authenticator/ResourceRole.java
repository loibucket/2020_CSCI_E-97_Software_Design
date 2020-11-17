package cscie97.smartcity.authenticator;

/**
 * The resource role defines what permission the user has for a specific resource
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-11-15
 */
public class ResourceRole extends Entitlement {
    /**
     * Specific entitlement to a resource in the model
     * <resource_role_name> <role> <resource>
     *
     * @param id          id
     * @param name        name
     * @param description optional description
     */
    public ResourceRole(String cityId, String id, String name, String description) {
        super(cityId, id, name, description, EntType.ResourceRole);
    }
}
