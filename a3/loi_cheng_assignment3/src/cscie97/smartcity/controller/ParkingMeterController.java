package cscie97.smartcity.controller;

import cscie97.smartcity.helper.Tool;
import cscie97.smartcity.model.*;

import java.util.List;
import java.util.Map;

/**
 * The parking meter controller observes parked cars
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class ParkingMeterController implements IoTObserver, CommandFactory {

    private final City city;
    private IoTDevice targetSpace;

    /**
     * constructor
     *
     * @param c the city it is controlling the devices
     */
    public ParkingMeterController(City c) {
        this.city = c;
    }

    /**
     * create command to charge the vehicle
     *
     * @return the charge vehicle action
     */
    @Override
    public Command createCommand() {
        return new ParkingEventCommand(this.targetSpace, this.city);
    }

    /**
     * Look for a car at the parking space
     *
     * @param space the parking space
     * @throws ServiceException if observation error
     */
    @Override
    public void observe(IoTDevice space) throws ServiceException {
        this.targetSpace = space;

        //only respond to parking space devices
        if (this.targetSpace.getClass().getName().equals("cscie97.smartcity.model.ParkingSpace")) {

            String footage = this.targetSpace.readSensor(SensorType.camera)[0];

            //only respond to parked vehicle on camera
            if (footage == null) {
                return;
            }

            if (footage.contains("Vehicle") && footage.contains("parked for") && footage.contains("hour")) {
                createCommand().execute();

            }
        }
    }
}
