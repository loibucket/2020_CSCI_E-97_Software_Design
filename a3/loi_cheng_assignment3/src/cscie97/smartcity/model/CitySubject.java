package cscie97.smartcity.model;

import cscie97.ledger.CommandProcessorException;

import java.util.List;

/**
 * The city subject interface has methods specific to attach and detach observers, and to notify observers
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-18
 */
public interface CitySubject {

    /**
     * add an observer to be notified
     *
     * @param observer an IoT observer, e.g. camera controller
     */
    void attachObs(IoTObserver observer);

    /**
     * remove an observer from notifications
     *
     * @param observer an IoT observer, e.g. camera controller
     */
    void detachObs(IoTObserver observer);

    /**
     * notify all observers with a list of devices that has been updated
     *
     * @param d the list of devices that has been updated
     * @throws ServiceException if notification errors
     */
    void notifyObs(IoTDevice d) throws ServiceException, CommandProcessorException;

}
