package cscie97.smartcity.model;

enum PersonType
{
	resident, visitor;
}
enum Role
{
	adult,child,administrator
}

public class Person {

	private String personId;
	private String biometricId;
	private float[] location;
	private PersonType type;
	private String name;
	private String phoneNumber;
	private Role role;
	private String blockchainAddress;

	public Person(PersonType type, String personId, String name, String biometricId, String phone, Role role,
	float[] location, String blockchainAddress){

		this.type = type;
		this.personId = personId;
		this.name = name;
		this.biometricId = biometricId;
		this.phoneNumber = phone;
		this.role = role;
		this.location = location;
		this.blockchainAddress = blockchainAddress;
	}

}
