package cscie97.smartcity.controller;

import cscie97.smartcity.authenticator.*;
import cscie97.smartcity.shared.*;
import cscie97.smartcity.model.*;

/**
 * Command to return movie info
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class MovieInfoCommand implements Command {

    private final InfoKiosk kiosk;

    /**
     * constructor
     *
     * @param kiosk the kiosk handling the request
     */
    public MovieInfoCommand(IoTDevice kiosk, City city) throws AuthException {
        this.kiosk = (InfoKiosk) kiosk;
    }

    /**
     * provide movie info
     *
     * @throws ServiceException if any errors
     */
    @Override
    public void execute() throws ServiceException, AuthException {

        //the kiosk
        Tool.report(kiosk);

        //update kiosk
        this.kiosk.sensorEvent(SensorType.speaker, "Casablanca is showing at 9pm", this.kiosk.readSensor(SensorType.microphone)[1]);
        this.kiosk.updateInfoKiosk(true, "https://en.wikipedia.org/wiki/Casablanca_(film)#/media/File:CasablancaPoster-Gold.jpg");
        this.kiosk.sensorEvent(SensorType.microphone, "Casablanca is showing at 9pm", null);

        //the kiosk
        Tool.report(kiosk);

    }
}
