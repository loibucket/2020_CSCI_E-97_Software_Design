public class City {

	private String cityId;

	private String name;

	private String blockchainAddress;

	private float[] location;

	private float radius;

	private Map<personId,Person> peopleMap;

	private Map<deviceId,IoTDevice> deviceMap;

	private StreetSign[] streetSign;

	private InfoKiosk[] infoKiosk;

	private StreetLight[] streetLight;

	private Robot[] robot;

	private ParkingSpace[] parkingSpace;

	private Vehicle[] vehicle;

	private Person[] person;

	public void defineStreetSign(deviceId device, bool enabled, String text, float[] location) {

	}

	public void defineInfoKiosk(deviceId device, bool enabled, uri image, float[] location) {

	}

	public void defineStreetLight(deviceId device, bool enabled, int brightness, float[] location) {

	}

	public void defineParkingSpace(deviceId device, bool enabled, int rate, float lat, float[] location) {

	}

	public void defineRobot(deviceId device, bool enabled, String activity, float lat, float[] location) {

	}

	public String showDevice(deviceId device) {
		return null;
	}

	public void createSensorEvent(String deviceId, String type, String value, personId subject) {

	}

	public void definePerson(String type, personId person, biometricId biometric, float[] location, String name, String phone, String role, blockchainAddress account) {

	}

	public void showPerson(personId person) {

	}

}
