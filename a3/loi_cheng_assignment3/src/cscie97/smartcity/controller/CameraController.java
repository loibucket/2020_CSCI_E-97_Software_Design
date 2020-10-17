package cscie97.smartcity.controller;

import cscie97.smartcity.model.IoTDevice;
import cscie97.smartcity.model.IoTObserver;

import java.util.List;

public class CameraController implements IoTObserver, CommandFactory {

	private EmergencyOneCommand emergencyOne;

	private EmergencyTwoCommand emergencyTwo;

	private LitterEventCommand litterEvent;

	private PersonSeenCommand personSeen;

	private BoardBusCommand boardBus;

	@Override
	public Command createCommand(List<IoTDevice> deviceList) {
		return null;
	}

	@Override
	public void observe(List<IoTDevice> deviceList) {

	}
}
