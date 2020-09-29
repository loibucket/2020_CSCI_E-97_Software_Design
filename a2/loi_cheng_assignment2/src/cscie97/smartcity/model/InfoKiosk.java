package cscie97.smartcity.model;

public class InfoKiosk extends IoTDevice {

	private String imageUri;

	// define info-kiosk <city_id>:<device_id> lat <float> long <float> enabled (true|false) image <uri>
	public InfoKiosk(String deviceId, float[] location, Boolean enabled, String imageUri){
		super.deviceId = deviceId;
		super.location = location;
		super.enabled = enabled;
		this.imageUri = imageUri;
	}

	public void updateInfoKiosk(Boolean enabled, String imageUri) {
		super.enabled = enabled;
		this.imageUri = imageUri;
	}

}
