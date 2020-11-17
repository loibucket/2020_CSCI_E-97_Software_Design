package cscie97.smartcity.authenticator;

/**
 * A role groups multiple permissions
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-11-15
 */
public class Role extends Entitlement {

    /**
     * Specific entitlement to a role in the model
     *
     * @param id          id
     * @param name        name
     * @param description optional description
     */
    public Role(String cityId, String id, String name, String description) {
        super(cityId, id, name, description, EntType.Role);
    }


}
