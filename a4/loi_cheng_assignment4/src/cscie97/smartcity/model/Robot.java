package cscie97.smartcity.model;

import cscie97.smartcity.authenticator.AuthException;
import cscie97.smartcity.authenticator.Authenticator;

import java.util.Arrays;

/**
 * Robot
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class Robot extends IoTDevice {

    /**
     * Robot
     *
     * @param deviceId robot id
     * @param location lat long
     * @param enabled  true false
     * @param activity last thing it is doing
     */
    public Robot(String deviceId, Float[] location, Boolean enabled, String activity) {
        // define robot <city_id>:<device_id> lat <Float> Float <Float> enabled(true|false) activity <string>
        super.deviceId = deviceId;
        super.location = location;
        super.enabled = enabled;
        super.event = activity;
    }

    /**
     * Update robot, do not update if null given
     *
     * @param location lat long
     * @param enabled  true false
     * @param activity last thing it is doing
     */
    public void updateRobot(Float[] location, Boolean enabled, String activity) throws AuthException {

        Authenticator.authenticate("updateRobot", this.deviceId);

        super.location = location == null ? super.location : location;
        super.enabled = enabled == null ? super.enabled : enabled;
        super.event = activity == null ? super.event : activity;
    }

    /**
     * To String
     *
     * @return string
     */
    @Override
    public String toString() {
        return "Robot{" +
                "deviceId='" + deviceId + '\'' +
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
