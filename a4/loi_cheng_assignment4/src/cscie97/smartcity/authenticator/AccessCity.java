package cscie97.smartcity.authenticator;

/**
 * Cities under permission management in authenticator
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-11-15
 */
public class AccessCity extends Entitlement {

    public AccessCity(String id, String name) {
        super(id, id, name, null, EntType.AccessCity);
    }

}
