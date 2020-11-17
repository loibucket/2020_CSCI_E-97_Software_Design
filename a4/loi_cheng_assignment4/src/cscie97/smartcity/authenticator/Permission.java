package cscie97.smartcity.authenticator;

/**
 * A permission allows a user to use a specific method in the city
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-11-15
 */
public class Permission extends Entitlement {

    /**
     * Specific entitlement to a method in the model
     *
     * @param id          id
     * @param name        name of method
     * @param description optional description
     */
    public Permission(String cityId, String id, String name, String description) {
        super(cityId, id, name, description, EntType.Permission);
    }

}
