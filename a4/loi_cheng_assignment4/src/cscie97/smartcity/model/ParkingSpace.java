package cscie97.smartcity.model;

import cscie97.smartcity.authenticator.AuthException;
import cscie97.smartcity.authenticator.Authenticator;

import java.util.Arrays;

/**
 * Parking Space
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class ParkingSpace extends IoTDevice {

    private Integer rate;

    //getter
    public Integer getRate() {
        return rate;
    }

    /**
     * constructor
     *
     * @param deviceId id
     * @param location lat long
     * @param enabled  boolean
     * @param rate     per hour charged for parking
     */
    public ParkingSpace(String deviceId, Float[] location, Boolean enabled, Integer rate) {
        // define parking-space <city_id>:<device_id> lat <Float> Float <Float> enabled(true|false) rate <Integer>
        super.deviceId = deviceId;
        super.location = location;
        super.enabled = enabled;
        this.rate = rate;
    }

    /**
     * @param enabled boolean
     * @param rate    per hour charged for parking
     */
    public void updateParkingSpace(Boolean enabled, Integer rate) throws AuthException {

        Authenticator.authenticate("updateParkingSpace", this.deviceId);

        super.enabled = enabled == null ? super.enabled : enabled;
        this.rate = rate == null ? this.rate : rate;
    }

    /**
     * To String
     *
     * @return string
     */
    @Override
    public String toString() {
        return "ParkingSpace{" +
                "rate=" + rate +
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
