package cscie97.smartcity.model;

enum SensorType {
    microphone, camera, thermometer, co2meter, speaker
}

/**
 * Sensor
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class Sensor {

    private final SensorType type;
    private String value;
    private String personId;

    /**
     * Constructor
     *
     * @param type mic camera thermo co2 speaker
     */
    public Sensor(SensorType type) {

        this.type = type;
        this.value = null;
        this.personId = null;
    }

    /**
     * Update sensor, fields can be null
     *
     * @param value    what it recorded or broadcasting
     * @param personId optional person it's doing on
     */
    public void updateSensor(String value, String personId) {
        this.value = value;
        this.personId = personId;
    }

    /**
     * To String
     *
     * @return string
     */
    @Override
    public String toString() {
        return "Sensor{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", personId='" + personId + '\'' +
                '}';
    }
}
