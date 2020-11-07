package cscie97.smartcity.controller;

import cscie97.smartcity.shared.*;
import cscie97.smartcity.model.*;

/**
 * Command for person seen events
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class PersonSeenCommand implements Command {

    private final IoTDevice device;
    private final Person person;

    /**
     * constructor
     *
     * @param targetDevice the device that saw the person
     * @throws ServiceException if person id not found in registry
     */
    public PersonSeenCommand(IoTDevice targetDevice, City c) throws ServiceException {
        this.device = targetDevice;
        try {
            this.person = ModelApi.getRegistry().showPerson(targetDevice.readSensor(SensorType.camera)[1]);
        } catch (Exception e) {
            throw new ServiceException("person seen", "person id not found in registry!");
        }
    }

    /**
     * execute, updates the person's location
     *
     * @throws ServiceException if error in updating location
     */
    @Override
    public void execute() throws ServiceException {
        Tool.report(this.device);
        Tool.report(this.person);
        try {
            person.updateResident(null, null, null, null, this.device.getLocation(), null);
            Tool.report(this.person);
        } catch (Exception e) {
            throw new ServiceException("person seen", "location update error!");
        }
    }
}
