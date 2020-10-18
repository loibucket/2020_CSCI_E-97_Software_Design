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
     *
     * @param type     type
     * @param action   activity / event
     * @param personId person (optional, null)
     */
    public void sensorEvent(SensorType type, String action, String personId) {
        switch (type) {
            case microphone -> this.microphone.updateEvent(action, personId);
            case camera -> this.camera.updateEvent(action, personId);
            case thermometer -> this.thermometer.updateEvent(action, personId);
            case co2meter -> this.co2meter.updateEvent(action, personId);
            case speaker -> this.speaker.updateEvent(action, personId);
        }
    }

    /**
     * Retrieve event recorded in sensor
     *
     * @param type sensor type
     * @return the recorded event as a string and the (optional) person id
     */
    public String[] readSensor(SensorType type) throws ServiceException {
        switch (type) {
            case microphone -> {
                return this.microphone.readEvent();
            }
            case camera -> {
                return this.camera.readEvent();
            }
            case thermometer -> {
                return this.thermometer.readEvent();
            }
            case co2meter -> {
                return this.co2meter.readEvent();
            }
            case speaker -> {
                return this.speaker.readEvent();
            }
        }
        throw new ServiceException("read sensor event","sensor error");
    }

    /**
     * Getter
     *
     * @return location
     */
    public Float[] getLocation() {
        return location;
    }
}
