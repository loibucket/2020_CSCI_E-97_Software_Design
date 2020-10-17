package cscie97.smartcity.controller;

import cscie97.smartcity.model.IoTDevice;
import cscie97.smartcity.model.IoTObserver;

import java.util.List;

public class COController implements IoTObserver, CommandFactory {

	private COTwoUnderCommand cOTwoUnder;

	private COTwoOverCommand cOTwoOver;

	@Override
	public Command createCommand(List<IoTDevice> deviceList) {
		return null;
	}

	@Override
	public void observe(List<IoTDevice> deviceList) {

	}
}
