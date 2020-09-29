package cscie97.smartcity.model;

public class ParkingSpace extends IoTDevice {

	private int rate;

	// define parking-space <city_id>:<device_id> lat <float> long <float> enabled(true|false) rate <int>
	public ParkingSpace(String deviceId, float[] location, Boolean enabled, int rate){
		super.deviceId = deviceId;
		super.location = location;
		super.enabled = enabled;
		this.rate = rate;
	}

	public void updateParkingSpace(Boolean enabled, int rate) {
		super.enabled = enabled;
		this.rate = rate;
	}
}
