package cscie97.smartcity.model;

import cscie97.smartcity.authenticator.AuthException;
import cscie97.smartcity.authenticator.AuthenticationApi;
import cscie97.smartcity.authenticator.Authenticator;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Peoples Registry
 * Maintains and updates all persons in the world
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class Registry {

    private final String registryId;
    private final Map<String, Person> personMap = new HashMap<>();

    /**
     * Constructor
     *
     * @param id id of registry
     */
    public Registry(String id) {
        this.registryId = id;
    }

    /**
     * to String
     *
     * @return info on the registry
     */
    @Override
    public String toString() {
        return "PeoplesRegistry{" +
                "peoplesId='" + registryId + '\'' +
                ", peoples=" + personMap +
                '}';
    }

    /**
     * Helper: Define a person
     *
     * @param type              resident or visitor
     * @param personId          id
     * @param name              name
     * @param biometricId       biometric id
     * @param phone             phone number
     * @param role              adult child administrator
     * @param location          latitude longitude
     * @param blockchainAddress account address
     * @throws ServiceException if unable to process
     */
    public void definePerson(PersonType type, String personId, String name, String biometricId, String phone,
                             String role, Float[] location, String blockchainAddress) throws ServiceException, AuthException, NoSuchAlgorithmException {

        Authenticator.authenticate("definePerson", personId);

        // error
        if (personMap.containsKey(personId)) {
            throw new ServiceException("define Person", "personId already exists!");
        }

        // convert role to enum
        Role roleType = null;
        if (type == PersonType.resident) {
            switch (role) {
                case "adult" -> roleType = Role.adult;
                case "child" -> roleType = Role.child;
                case "administrator" -> roleType = Role.administrator;
                default -> throw new ServiceException("define Person", "unrecognized role!");
            }
        }

        // create person and add to map
        Person person = new Person(type, personId, name, biometricId, phone, roleType, location, blockchainAddress);
        personMap.put(personId, person);

        // create person and add to user base
        AuthenticationApi.processCommand("create_user " + personId + " " + name, -1);
        AuthenticationApi.processCommand("add_user_credential " + personId + " biometric" + " " + "voiceprint:" + name + "-voiceprint", -1);
        AuthenticationApi.processCommand("add_user_credential " + personId + " biometric" + " " + "faceprint:" + name + "-faceprint", -1);

        switch (role) {
            case "adult" -> {
                AuthenticationApi.processCommand("add_role_to_user " + personId + " adult", -1);
            }
            case "child" -> {
                AuthenticationApi.processCommand("add_role_to_user " + personId + " child", -1);
            }
            case "administrator" -> {
                AuthenticationApi.processCommand("add_role_to_user " + personId + " adult", -1);
                AuthenticationApi.processCommand("add_role_to_user " + personId + " administrator", -1);
            }
            default -> throw new ServiceException("define Person", "unrecognized role!");
        }
    }

    /**
     * Get all people
     *
     * @return map of id's and persons
     */
    public Map<String, Person> showAllPersons() {
        return this.personMap;
    }

    /**
     * Show person with id
     *
     * @param personId id of person
     * @return Person object
     * @throws ServiceException if cannot find person
     */
    public Person showPerson(String personId) throws ServiceException {
        if (personMap.get(personId) == null) {
            throw new ServiceException("show person", "person not found!");
        } else {
            //System.out.println(personMap.get(personId));
        }
        return personMap.get(personId);
    }

}
