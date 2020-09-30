package cscie97.smartcity.model;

/**
 * IoTDevice is the base of all the other devices like robot, parking space, etc.
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class IoTDevice {

	protected String deviceId;
	protected Float[] location;
	protected Boolean enabled;
	protected String event;
	protected Sensor microphone = new Sensor(SensorType.microphone);
	protected Sensor camera = new Sensor(SensorType.camera);
	protected Sensor thermometer = new Sensor(SensorType.thermometer);
	protected Sensor co2meter = new Sensor(SensorType.co2meter);
	protected Sensor speaker = new Sensor(SensorType.speaker);

	/**
	 * Update sensor
	 * @param type type
	 * @param action activity / event
	 * @param personId person (optional, null)
	 */
	public void sensorEvent(SensorType type, String action, String personId){
		switch(type){
			case microphone -> this.microphone.updateSensor(action,personId);
			case camera -> this.camera.updateSensor(action,personId);
			case thermometer -> this.thermometer.updateSensor(action,personId);
			case co2meter -> this.co2meter.updateSensor(action,personId);
			case speaker -> this.speaker.updateSensor(action,personId);
		}
	}
}
