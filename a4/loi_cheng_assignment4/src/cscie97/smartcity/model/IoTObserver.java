package cscie97.smartcity.model;

import cscie97.ledger.CommandProcessorException;
import cscie97.smartcity.model.IoTDevice;
import cscie97.smartcity.model.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * The IoT observer interface looks for specific events in the device list and acts on it through the devices in the device map
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-18
 */
public interface IoTObserver {

    /**
     * Look through the events stored in the given device list
     *
     * @param device the device check events from
     * @throws ServiceException if obeserving errors
     */
    default void observe(IoTDevice device) throws ServiceException {

    }

}
