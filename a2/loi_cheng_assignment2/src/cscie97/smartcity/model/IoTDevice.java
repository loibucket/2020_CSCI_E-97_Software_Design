package cscie97.smartcity.model;

public class IoTDevice {

	protected String deviceId;
	protected float[] location;
	protected Boolean enabled;
	protected String event;
	protected Sensor microphone = new Sensor(SensorType.microphone);
	protected Sensor camera = new Sensor(SensorType.camera);
	protected Sensor thermometer = new Sensor(SensorType.thermometer);
	protected Sensor co2meter = new Sensor(SensorType.co2meter);
	protected Sensor speaker = new Sensor(SensorType.speaker);

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
