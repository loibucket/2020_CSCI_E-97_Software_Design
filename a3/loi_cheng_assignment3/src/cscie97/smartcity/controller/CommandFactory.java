package cscie97.smartcity.controller;

import cscie97.smartcity.model.IoTDevice;

import java.util.List;

public interface CommandFactory {

	Command createCommand(List<IoTDevice> deviceList);

}
