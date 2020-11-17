package cscie97.smartcity.authenticator;

/**
 * The resource specifies the single item that user has access to
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-11-15
 */
public class Resource extends Entitlement {

    /**
     * Specific entitlement to a resource in the model
     *
     * @param id          id
     * @param name        name
     * @param description optional description
     */
    public Resource(String cityId, String id, String name, String description) {
        super(cityId, id, name, description, EntType.Resource);
    }

}
