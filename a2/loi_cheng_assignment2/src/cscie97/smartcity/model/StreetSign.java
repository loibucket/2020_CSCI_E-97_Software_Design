package cscie97.smartcity.model;

public class StreetSign extends IoTDevice {

	private String text;

	// define street-sign <city_id>:<device_id> lat <float> long <float> enabled (true|false) text <text>
	public StreetSign(String deviceId, float[] location, Boolean enabled, String text){
		super.deviceId = deviceId;
		super.location = location;
		super.enabled = enabled;
		this.text = text;
	}

	public void updateStreetSign(Boolean enabled, String text) {
		super.enabled = enabled;
		this.text = text;
	}

}
