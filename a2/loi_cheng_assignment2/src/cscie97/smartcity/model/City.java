package cscie97.smartcity.model;

import java.util.Map;
import java.util.Arrays;

public class City {

	private String cityId;

	private String name;

	private String blockchainAddress;

	private float[] location;

	private float radius;

	private Map<String, IoTDevice> deviceMap;

	private Map<String, Person> peopleMap;


	public City(String cityId, String name, String account, float[] location, float radius){
		System.out.println(cityId+name+account+Arrays.toString(location)+radius);
	}

	public void defineStreetSign(String deviceId, boolean enabled, String text, float[] location) {

	}

	public void defineInfoKiosk(String deviceId, boolean enabled, String uri, float[] location) {

	}

	public void defineStreetLight(String deviceId, boolean enabled, int brightness, float[] location) {

	}

	public void defineParkingSpace(String deviceId, boolean enabled, int rate, float lat, float[] location) {

	}

	public void defineRobot(String deviceId, boolean enabled, String activity, float lat, float[] location) {

	}

	public String showDevice(String deviceId) {
		return null;
	}

	public void createSensorEvent(String deviceId, String type, String value, String personId) {

	}

	public void definePerson(String type, String personId, String biometricId, float[] location, String name,
							 String phone, String role, String blockchainAddress) {

	}

	public void showPerson(String personId) {

	}

}
