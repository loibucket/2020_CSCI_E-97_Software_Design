package cscie97.smartcity.controller;

import cscie97.smartcity.model.IoTObserver;

public class CameraController implements IoTObserver, CommandFactory {

	private EmergencyOne emergencyOne;

	private EmergencyTwo emergencyTwo;

	private LitterEvent litterEvent;

	private PersonSeen personSeen;

	private BoardBus boardBus;

}
