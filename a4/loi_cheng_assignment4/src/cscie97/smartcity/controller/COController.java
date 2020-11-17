package cscie97.smartcity.controller;

import cscie97.smartcity.authenticator.AuthException;
import cscie97.smartcity.authenticator.AuthToken;
import cscie97.smartcity.model.*;
import cscie97.smartcity.model.IoTObserver;

import java.lang.Integer;

/**
 * The CO controller observes events from the CO meter and responds accordingly
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class COController implements IoTObserver, CommandFactory {

    private City city;
    private int coCount;
    Boolean carsEnabled;

    /**
     * constructor
     *
     * @param c the city
     */
    public COController(City c) {
        this.city = c;
        this.coCount = 0; //default to no meters over 1000
        this.carsEnabled = true; //default to all cars enabled
    }

    @Override
    public Command createCommand() {
        return new COCommand(carsEnabled, this.city);
    }

    /**
     * Observer, disables all cars if it sees 3 consecutive CO>1000, enables all cars if it sees 3 consecutive CO<1000
     *
     * @param d the device to check all the events from
     * @throws ServiceException if error
     */
    @Override
    public void observe(IoTDevice d) throws ServiceException, AuthException {

        //read co2 level
        String coReading = d.readSensor(SensorType.co2meter)[0];
        if (coReading == null) {
            return;
        }

        //convert to int
        int coLevel;
        try {
            coLevel = Integer.parseInt(coReading);
        } catch (Exception e) {
            throw new ServiceException("co controller", "cannot read co level!");
        }

        //adjust recorded conditions
        if (coLevel >= 1000) {
            this.coCount = Integer.min(this.coCount + 1, 3);
        } else {
            this.coCount = Integer.max(this.coCount - 1, 0);
        }

        //take action
        if (this.coCount >= 3) {
            if (this.carsEnabled) {
                this.carsEnabled = false;
                System.out.println("-CO CONTROLLER ACTIVATED-");
                createCommand().execute();
            }
        } else if (coCount == 0) {
            if (!this.carsEnabled) {
                this.carsEnabled = true;
                System.out.println("-CO CONTROLLER ACTIVATED-");
                createCommand().execute();
            }
        }
    }
}
