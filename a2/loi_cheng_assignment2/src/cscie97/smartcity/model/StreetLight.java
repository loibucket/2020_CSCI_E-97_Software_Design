package cscie97.smartcity.model;

public class StreetLight extends IoTDevice {

	private int brightness;

	// define street-light <city_id>:<device_id> lat <float> long <float> enabled (true|false) brightness <int>
	public StreetLight(String deviceId, float[] location, Boolean enabled, int brightness){
		super.deviceId = deviceId;
		super.location = location;
		super.enabled = enabled;
		this.brightness = brightness;
	}
	public void updateStreetLight(Boolean enabled, int brightness) {
		super.enabled = enabled;
		this.brightness = brightness;
	}

}
