package cscie97.smartcity.model;
import java.util.ArrayList;
import java.util.List;

public interface IoTObserver {

	void observe(List<IoTDevice> deviceList);

}
