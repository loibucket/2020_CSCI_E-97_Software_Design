package cscie97.smartcity.controller;

import cscie97.smartcity.model.IoTDevice;
import cscie97.smartcity.model.IoTObserver;

import java.util.List;
import java.util.Map;

public class COController implements IoTObserver, CommandFactory {

	private COTwoUnderCommand cOTwoUnder;

	private COTwoOverCommand cOTwoOver;

	@Override
	public Command createCommand() {
		return null;
	}

	@Override
	public void observe(List<IoTDevice> deviceList, Map<String, IoTDevice> deviceMap) {
		//System.out.println("cocontoller:"+deviceList.toString());
	}
}
