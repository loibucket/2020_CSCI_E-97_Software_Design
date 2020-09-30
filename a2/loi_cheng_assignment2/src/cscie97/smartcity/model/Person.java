package cscie97.smartcity.model;

import java.util.Arrays;

enum PersonType
{
	resident, visitor;
}
enum Role
{
	adult,child,administrator
}

/**
 * Person
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class Person {

	private String personId;
	private String biometricId;
	private Float[] location;
	private PersonType type;
	private String name;
	private String phoneNumber;
	private Role role;
	private String blockchainAddress;

	/**
	 * Constructor
	 * @param type resident or visitor
	 * @param personId id
	 * @param name name
	 * @param biometricId biometric id
	 * @param phone phone number
	 * @param role adult child administrator
	 * @param location lat long
	 * @param blockchainAddress account
	 */
	public Person(PersonType type, String personId, String name, String biometricId, String phone, Role role,
	Float[] location, String blockchainAddress){

		this.type = type;
		this.personId = personId;
		this.name = name;
		this.biometricId = biometricId;
		this.phoneNumber = phone;
		this.role = role;
		this.location = location;
		this.blockchainAddress = blockchainAddress;
	}

	/**
	 * Update resident
	 * @param name name
	 * @param biometricId biometric
	 * @param phoneNumber phone number
	 * @param role adult child administrator
	 * @param location lat long
	 * @param account blockchain address
	 * @throws ServiceException if update failed
	 */
	public void updateResident(String name, String biometricId, String phoneNumber, Role role, Float[] location, String account) throws ServiceException {
		// update resident <person_id> [name <name>] [bio-metric <string>] [phone<phone_number>] [role (adult|child|administrator)] [lat <lat> Float <Float>] [account <account_address>]
		if (this.type != PersonType.resident){
			throw new ServiceException("upate resident","not a resident!");
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
	 * @param biometricId biometric
	 * @param location lat long
	 * @throws ServiceException if update failed
	 */
	public void updateVisitor(String biometricId, Float[] location) throws ServiceException {
		// update visitor <person_id> [bio-metric <string>] [lat <lat> Float <Float>]
		if (this.type != PersonType.visitor){
			throw new ServiceException("upate visitor","not a visitor!");
		}
		this.biometricId = biometricId == null ? this.biometricId : biometricId;
		this.location = location == null ? this.location : location;
	}

	/**
	 * To String
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
