package cscie97.smartcity.model;

enum VehicleType{
	car, bus;
}

public class Vehicle extends IoTDevice {

	private VehicleType type;
	private int capacity;
	private int fee;

	// define vehicle <city_id>:<device_id> lat <float> long <float> enabled(true|false) type (bus|car) activity <string> capacity <int> fee <int>
	public Vehicle(String deviceId, float[] location, Boolean enabled, VehicleType type, String activity, int capacity, int fee){
		super.deviceId = deviceId;
		super.location = location;
		super.enabled = enabled;
		this.type = type;
		super.event = activity;
		this.capacity = capacity;
		this.fee = fee;
	}

	public void updateVehicle(float[] location, Boolean enabled, String activity, int fee) {
		super.location = location;
		super.enabled = enabled;
		super.event = activity;
		this.fee = fee;
	}

}
