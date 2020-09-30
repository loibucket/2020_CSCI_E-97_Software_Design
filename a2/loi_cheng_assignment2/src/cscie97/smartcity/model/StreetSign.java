package cscie97.smartcity.model;

import java.util.Arrays;

/**
 * Street Sign
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class StreetSign extends IoTDevice {

    private String text;

    /**
     * Constructor
     *
     * @param deviceId id
     * @param location lat long
     * @param enabled  boolean
     * @param text     what the sign says
     */
    public StreetSign(String deviceId, Float[] location, Boolean enabled, String text) {
        // define street-sign <city_id>:<device_id> lat <float> Float <float> enabled (true|false) text <text>
        super.deviceId = deviceId;
        super.location = location;
        super.enabled = enabled;
        this.text = text;
    }

    /**
     * Update, no change if null given
     *
     * @param enabled boolean
     * @param text    what the sign says
     */
    public void updateStreetSign(Boolean enabled, String text) {
        super.enabled = enabled == null ? super.enabled : enabled;
        this.text = text == null ? this.text : text;
    }

    /**
     * To String
     *
     * @return string
     */
    @Override
    public String toString() {
        return "StreetSign{" +
                "text='" + text + '\'' +
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
