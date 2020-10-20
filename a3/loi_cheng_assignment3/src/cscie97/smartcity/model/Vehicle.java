package cscie97.smartcity.model;

import java.util.Arrays;

/**
 * Vehicle
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class Vehicle extends IoTDevice {

    private final VehicleType type;
    private final Integer capacity;
    private Integer fee;

    //getters
    public Integer getCapacity() {
        return capacity;
    }

    public Integer getFee() {
        return fee;
    }

    public VehicleType getType() {
        return type;
    }
    //getters

    /**
     * Constructor
     *
     * @param deviceId id
     * @param location lat long
     * @param enabled  bool
     * @param type     car bus
     * @param activity what it's doing
     * @param capacity how many seats
     * @param fee      cost per passenger
     */
    public Vehicle(String deviceId, Float[] location, Boolean enabled, VehicleType type, String activity, Integer capacity, Integer fee) {
        // define vehicle <city_id>:<device_id> lat <Float> Float <Float> enabled(true|false) type (bus|car) activity <string> capacity <int> fee <int>
        super.deviceId = deviceId;
        super.location = location;
        super.enabled = enabled;
        this.type = type;
        super.event = activity;
        this.capacity = capacity;
        this.fee = fee;
    }

    /**
     * Update, except if null given
     *
     * @param location lat long
     * @param enabled  bool
     * @param activity what it's doing
     * @param fee      cost per passenger
     */
    public void updateVehicle(Float[] location, Boolean enabled, String activity, Integer fee) {
        super.location = location == null ? super.location : location;
        super.enabled = enabled == null ? super.enabled : enabled;
        super.event = activity == null ? super.event : activity;
        this.fee = fee == null ? this.fee : fee;
    }

    /**
     * To String
     *
     * @return string
     */
    @Override
    public String toString() {
        return "Vehicle{" +
                "type=" + type +
                ", capacity=" + capacity +
                ", fee=" + fee +
                ", deviceId='" + deviceId + '\'' +
                ", location=" + Arrays.toString(location) +
                ", enabled=" + enabled +
                ", event='" + event + '\'' +
                ", microphone=" + microphone +
                ", camera=" + camera +
                ", thermometer=" + thermometer +
                ", co2meter=" + co2meter +
                ", speaker=" + speaker +
                '}';
    }
}
