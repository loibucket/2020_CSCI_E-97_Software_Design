package cscie97.smartcity.model;

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
    public void updateEvent(String value, String personId) {
        this.value = value;
        this.personId = personId;
    }

    /**
     * read sensor, fields can be null
     */
    public String[] readEvent() {
        return(new String[]{this.value,this.personId});
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
