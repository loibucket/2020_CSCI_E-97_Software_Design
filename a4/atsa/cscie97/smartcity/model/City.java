package cscie97.smartcity.model;

import cscie97.smartcity.controller.ServiceException;
import cscie97.smartcity.authenticator.AuthenticationAPI;

public class City implements CitySubject {

	private String cityId;

	private String name;

	private String blockchainAddress;

	private float[] location;

	private float radius;

	private Map<deviceId,IoTDevice> deviceMap;

	private StreetSign[] streetSign;

	private InfoKiosk[] infoKiosk;

	private StreetLight[] streetLight;

	private Robot[] robot;

	private ParkingSpace[] parkingSpace;

	private Vehicle[] vehicle;

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

}
