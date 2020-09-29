package cscie97.smartcity.model;

enum SensorType {
	microphone, camera, thermometer, co2meter, speaker
}

public class Sensor {

	private SensorType type;
	private String action;
	private String personId;

	public Sensor(SensorType type){
		this.type = type;
	}

	public void updateSensor(String action, String personId){
		this.action = action;
		this.personId = personId;
	}

}
