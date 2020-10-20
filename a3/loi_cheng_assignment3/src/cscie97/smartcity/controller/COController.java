package cscie97.smartcity.controller;

import cscie97.smartcity.model.*;

import java.util.Map;
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

    public COController(City c) {
        coCount = 0;
        carsEnabled = true;
    }

    @Override
    public Command createCommand() {
        if (this.coCount > 0 && this.coCount < 3) {
            return new COCommand(null, this.city);
        }
        if (this.coCount >= 3) {
            if (this.carsEnabled) {
                this.carsEnabled = false;
            }
        } else if (coCount == 0) {
            if (!this.carsEnabled) {
                this.carsEnabled = true;
            }
        }
        return new COCommand(this.carsEnabled, this.city);
    }

    /**
     * Observer, disables all cars if it sees 3 consecutive CO>1000, enables all cars if it sees 3 consecutive CO<1000
     *
     * @param d the device to check all the events from
     * @throws ServiceException if error
     */
    @Override
    public void observe(IoTDevice d) throws ServiceException {
        int coLevel = Integer.parseInt(d.readSensor(SensorType.co2meter)[0]);
        if (coLevel >= 1000) {
            this.coCount = Integer.min(this.coCount++, 3);
        } else {
            this.coCount = Integer.max(this.coCount--, 0);
        }
        createCommand().execute();
    }

}
