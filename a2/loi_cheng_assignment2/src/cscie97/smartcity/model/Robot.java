package cscie97.smartcity.model;

public class Robot extends IoTDevice {

	// define robot <city_id>:<device_id> lat <float> long <float> enabled(true|false) activity <string>
	public Robot(String deviceId, float[] location, Boolean enabled, String activity) {
		super.deviceId = deviceId;
		super.location = location;
		super.enabled = enabled;
		super.event = activity;
	}

	public void updateRobot(float[] location, Boolean enabled, String activity) {
		super.location = location;
		super.enabled = enabled;
		super.event = activity;
	}

}
