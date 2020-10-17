package cscie97.smartcity.controller;

import cscie97.smartcity.model.IoTDevice;
import cscie97.smartcity.model.IoTObserver;

import java.util.List;

public class MicController implements IoTObserver, CommandFactory {

	private MissingChildCommand missingChild;

	private BusRouteCommand busRoute;

	private BrokenGlassCommand brokenGlass;

	@Override
	public Command createCommand(List<IoTDevice> deviceList) {
		return null;
	}

	@Override
	public void observe(List<IoTDevice> deviceList) {

	}
}
