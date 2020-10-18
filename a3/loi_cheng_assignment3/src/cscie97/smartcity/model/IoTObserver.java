package cscie97.smartcity.model;
import java.util.List;
import java.util.Map;

public interface IoTObserver {

	void observe(List<IoTDevice> deviceList, Map<String, IoTDevice> deviceMap) throws ServiceException;

}
