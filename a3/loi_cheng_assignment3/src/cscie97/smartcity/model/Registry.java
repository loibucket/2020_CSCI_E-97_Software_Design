package cscie97.smartcity.model;

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
     * @param id id of registry
     */
    public Registry(String id){
        this.registryId = id;
    }

    /** to String
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
                              String role, Float[] location, String blockchainAddress) throws ServiceException {

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

    }

    /**
     * Get all people
     * @return map of id's and persons
     */
    public Map<String, Person> showAllPersons(){
        return this.personMap;
    }

    /**
     * Show person with id
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
