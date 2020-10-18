package cscie97.smartcity.model;

import java.util.List;

public interface CitySubject {

	void attachObs(IoTObserver observer);

	void detachObs(IoTObserver observer);

	void notifyObs(List<IoTDevice> deviceList) throws ServiceException;

}
