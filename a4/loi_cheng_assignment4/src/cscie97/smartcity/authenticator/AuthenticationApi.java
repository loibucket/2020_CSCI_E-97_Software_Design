package cscie97.smartcity.authenticator;

import cscie97.ledger.LedgerApi;
import cscie97.smartcity.model.ModelException;
import cscie97.smartcity.shared.Tool;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 * Process command lines related to authentication
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-11-15
 */
public class AuthenticationApi {

    /**
     * process a line of command
     *
     * @param command    command
     * @param lineNumber line number if given
     */
    public static void processCommand(String command, int lineNumber) throws AuthException, NoSuchAlgorithmException {

        if (Authenticator.getAccessToCity() == null) {
            System.out.println("AUTHAPI: " + command);
        } else {
            System.out.println("AUTHAPI/" + Authenticator.getAccessToCity().getId() + ": " + command);
        }

        // replace special quotes to normal
        command = command.replace('“', '"');
        command = command.replace('”', '"');

        // split string by whitespace, except when between quotes
        // stackoverflow.com/questions/18893390/splitting-on-comma-outside-quotes
        List<String> a = Arrays.asList(command.split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));

        for (int i = 0; i < a.size(); i++) {
            a.set(i, Tool.clean(a.get(i)));
        }

        // do different action with commands
        String action = a.get(0);


        try {
            switch (action) {
                case "define_permission" -> Authenticator.definePermission(a.get(1), a.get(2), a.get(3));
                case "define_role" -> Authenticator.defineRole(a.get(1), a.get(2), a.get(3));
                case "add_permission_to_role", "add_role_to_user", "add_role_to_role", "add_resource_to_role" -> Authenticator.addSub(a.get(1), a.get(2));
                case "create_user" -> Authenticator.createUser(a.get(1), a.get(2));
                case "add_user_credential" -> Authenticator.addUserCredential(a.get(1), a.get(2), a.get(3));
                case "create_resource_role" -> Authenticator.createResourceRole(a.get(1), a.get(2), a.get(3));
                case "create_resource" -> Authenticator.createResource(a.get(1), a.get(2), a.get(3));
                case "show_entitlements" -> Authenticator.showEntitlements(a.get(1));
                case "show_all_entitlements" -> Authenticator.showAllEntitlements();
                case "set_city_access" -> Authenticator.setCityAccess(a.get(1));
                case "create_city_access" -> Authenticator.createAccessToCity(a.get(1), a.get(2));
                default -> LedgerApi.processCommand(command, lineNumber); //try ledger commands
            }
        } catch (AuthException e) {
            System.out.println(e.toString());
            //e.printStackTrace();
        } catch (Exception e) {
            System.out.println(new ModelException(command, "other", e.toString(), lineNumber).toString());
            //e.printStackTrace();
        }
    }

}
