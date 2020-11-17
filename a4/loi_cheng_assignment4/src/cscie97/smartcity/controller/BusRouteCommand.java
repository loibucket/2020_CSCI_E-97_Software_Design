package cscie97.smartcity.controller;

import cscie97.smartcity.authenticator.*;
import cscie97.smartcity.shared.Tool;
import cscie97.smartcity.model.*;

/**
 * Command for bus route request
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class BusRouteCommand implements Command {

    private final IoTDevice device;

    /**
     * constructor
     *
     * @param device the device that heard the request
     * @param city   the city where it happened
     */
    public BusRouteCommand(IoTDevice device, City city) {
        this.device = device;
    }

    /**
     * Give response about bus route
     *
     * @throws ServiceException if error in command creation
     */
    @Override
    public void execute() throws ServiceException, AuthException {

        //the device
        Tool.report(this.device);

        //the place
        String req = this.device.readSensor(SensorType.microphone)[0];
        req = req.substring(20, req.length() - 1);

        //update with response
        this.device.sensorEvent(SensorType.speaker, "yes this bus goes to " + req, this.device.readSensor(SensorType.microphone)[1]);
        this.device.sensorEvent(SensorType.microphone, "silence", null);
        Tool.report(this.device);

    }
}
