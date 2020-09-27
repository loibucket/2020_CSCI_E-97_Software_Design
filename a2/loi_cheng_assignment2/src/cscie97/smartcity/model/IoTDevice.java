package cscie97.smartcity.model;

import java.util.Map;

public class IoTDevice {

	private String deviceId;

	private float[] location;

	private boolean status;

	private boolean enabled;

	private String event;

	private Map<String,Sensor> sensorMap;

}
