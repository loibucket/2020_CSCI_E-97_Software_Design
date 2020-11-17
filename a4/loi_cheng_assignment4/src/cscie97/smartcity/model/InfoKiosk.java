package cscie97.smartcity.model;

import cscie97.smartcity.authenticator.AuthException;
import cscie97.smartcity.authenticator.Authenticator;

import java.util.Arrays;

/**
 * Information Kiosk
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class InfoKiosk extends IoTDevice {

    private String imageUri;

    /**
     * Constructor
     *
     * @param deviceId device Id
     * @param location lat long
     * @param enabled  true false
     * @param imageUri image
     */
    public InfoKiosk(String deviceId, Float[] location, Boolean enabled, String imageUri) {
        // define info-kiosk <city_id>:<device_id> lat <Float> Float <Float> enabled (true|false) image <uri>
        super.deviceId = deviceId;
        super.location = location;
        super.enabled = enabled;
        this.imageUri = imageUri;
    }

    /**
     * update, do not update if null is given
     *
     * @param enabled  true false
     * @param imageUri image
     */
    public void updateInfoKiosk(Boolean enabled, String imageUri) throws AuthException {

        Authenticator.authenticate("updateInfoKiosk", this.deviceId);

        super.enabled = enabled == null ? super.enabled : enabled;
        this.imageUri = imageUri == null ? this.imageUri : imageUri;
    }

    /**
     * To String
     *
     * @return string
     */
    @Override
    public String toString() {
        return "InfoKiosk{" +
                "imageUri='" + imageUri + '\'' +
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
