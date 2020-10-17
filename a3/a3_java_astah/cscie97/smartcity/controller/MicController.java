package cscie97.smartcity.controller;

import cscie97.smartcity.model.IoTObserver;

public class MicController implements IoTObserver, CommandFactory {

	private MissingChild missingChild;

	private BusRoute busRoute;

	private BrokenGlass brokenGlass;

}
