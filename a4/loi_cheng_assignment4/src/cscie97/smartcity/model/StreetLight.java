package cscie97.smartcity.model;

import cscie97.smartcity.authenticator.AuthException;
import cscie97.smartcity.authenticator.Authenticator;

import java.util.Arrays;

/**
 * Street Light
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class StreetLight extends IoTDevice {

    private Integer brightness;

    /**
     * Constructor
     *
     * @param deviceId   id
     * @param location   lat long
     * @param enabled    true false
     * @param brightness 0-100
     */
    public StreetLight(String deviceId, Float[] location, Boolean enabled, Integer brightness) throws ServiceException {
        // define street-light <city_id>:<device_id> lat <float> Float <float> enabled (true|false) brightness <int>
        if (brightness < 0 || brightness > 100) {
            throw new ServiceException("set street light brightness", "value outside 0-100 range!");
        }
        super.deviceId = deviceId;
        super.location = location;
        super.enabled = enabled;
        this.brightness = brightness;
    }

    /**
     * Update, do not update if null given
     *
     * @param enabled    true false
     * @param brightness 0-10
     */
    public void updateStreetLight(Boolean enabled, Integer brightness) throws ServiceException, AuthException {

        Authenticator.authenticate("updateStreetLight", this.deviceId);

        if (brightness < 0 || brightness > 100) {
            throw new ServiceException("set street light brightness", "value outside 0-100 range!");
        }
        super.enabled = enabled == null ? super.enabled : enabled;
        this.brightness = brightness == null ? this.brightness : brightness;
    }

    /**
     * To String
     *
     * @return string
     */
    @Override
    public String toString() {
        return "StreetLight{" +
                "brightness=" + brightness +
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
