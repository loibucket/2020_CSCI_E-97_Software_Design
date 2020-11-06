package cscie97.smartcity.model;

import cscie97.smartcity.controller.ServiceException;

public class Registry {

	private Map<personId,Person> peopleMap;

	private Person[] person;

	public void definePerson(String type, personId person, biometricId biometric, float[] location, String name, String phone, String role, blockchainAddress account) {

	}

	public Person showPerson(personId person) {
		return null;
	}

}
