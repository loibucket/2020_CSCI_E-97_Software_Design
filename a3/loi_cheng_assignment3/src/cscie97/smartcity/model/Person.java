package cscie97.smartcity.model;

import java.util.Arrays;

/**
 * Person
 *
 * @author Loi Cheng
 * <p>
 * v1.0 2020-09-29 initial
 * v1.1 2020-10-19 added more getters
 * @version 1.1
 * @since 2020-10-19
 */
public class Person {

    private final String personId;
    private String biometricId;
    private Float[] location;
    private final PersonType type;
    private String name;
    private String phoneNumber;
    private Role role;
    private String blockchainAddress;

    /**
     * Constructor
     *
     * @param type              resident or visitor
     * @param personId          id
     * @param name              name
     * @param biometricId       biometric id
     * @param phone             phone number
     * @param role              adult child administrator
     * @param location          lat long
     * @param blockchainAddress account
     */
    public Person(PersonType type, String personId, String name, String biometricId, String phone, Role role,
                  Float[] location, String blockchainAddress) {

        this.type = type;
        this.personId = personId;
        this.name = name;
        this.biometricId = biometricId;
        this.phoneNumber = phone;
        this.role = role;
        this.location = location;
        this.blockchainAddress = blockchainAddress;
    }


    //getters
    public PersonType getType() {
        return this.type;
    }

    public String getBlockchainAddress() {
        return blockchainAddress;
    }

    public String getBiometricId() {
        return biometricId;
    }

    public Role getRole() {
        return role;
    }

    public String getPersonId() {
        return personId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Float[] getLocation() {
        return this.location;
    }
    //getters

    /**
     * Update resident
     *
     * @param name        name
     * @param biometricId biometric
     * @param phoneNumber phone number
     * @param role        adult child administrator
     * @param location    lat long
     * @param account     blockchain address
     * @throws ServiceException if update failed
     */
    public void updateResident(String name, String biometricId, String phoneNumber, Role role, Float[] location, String account) throws ServiceException {
        // update resident <person_id> [name <name>] [bio-metric <string>] [phone<phone_number>] [role (adult|child|administrator)] [lat <lat> Float <Float>] [account <account_address>]
        if (this.type != PersonType.resident) {
            throw new ServiceException("update resident", "not a resident!");
        }
        // if null do not update, else update
        this.name = name == null ? this.name : name;
        this.biometricId = biometricId == null ? this.biometricId : biometricId;
        this.phoneNumber = phoneNumber == null ? this.phoneNumber : phoneNumber;
        this.role = role == null ? this.role : role;
        this.location = location == null ? this.location : location;
        this.blockchainAddress = account == null ? this.blockchainAddress : account;
    }

    /**
     * Update visitor
     *
     * @param biometricId biometric
     * @param location    lat long
     * @throws ServiceException if update failed
     */
    public void updateVisitor(String biometricId, Float[] location) throws ServiceException {
        // update visitor <person_id> [bio-metric <string>] [lat <lat> Float <Float>]
        if (this.type != PersonType.visitor) {
            throw new ServiceException("update visitor", "not a visitor!");
        }
        this.biometricId = biometricId == null ? this.biometricId : biometricId;
        this.location = location == null ? this.location : location;
    }

    /**
     * To String
     *
     * @return person string
     */
    @Override
    public String toString() {
        return "Person{" +
                "personId='" + personId + '\'' +
                ", biometricId='" + biometricId + '\'' +
                ", location=" + Arrays.toString(location) +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                ", blockchainAddress='" + blockchainAddress + '\'' +
                '}';
    }
}
