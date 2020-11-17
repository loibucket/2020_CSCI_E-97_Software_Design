package cscie97.smartcity.controller;

import cscie97.smartcity.authenticator.*;
import cscie97.smartcity.shared.Tool;
import cscie97.smartcity.model.City;
import cscie97.smartcity.model.IoTDevice;
import cscie97.smartcity.model.Vehicle;
import cscie97.smartcity.model.VehicleType;


/**
 * Command to react based on CO2 levels
 * enables or disables cars
 */
public class COCommand implements Command {

    City city;
    Boolean enable;

    /**
     * constructor
     *
     * @param enable boolean, true to enable all cars, false to disable
     * @param c      the city for the cars
     */
    public COCommand(Boolean enable, City c) {
        this.city = c;
        this.enable = enable;
    }

    /**
     * enables or disables all cars
     */
    @Override
    public void execute() throws AuthException {
        // find all the cars and update them
        for (IoTDevice d : this.city.showAllDevices().values()) {
            if (d.getClass().getName().equals("cscie97.smartcity.model.Vehicle") && ((Vehicle) d).getType() == VehicleType.car) {
                ((Vehicle) d).updateVehicle(null, this.enable, null, null);
                Tool.report(d);
            }
        }
    }
}
