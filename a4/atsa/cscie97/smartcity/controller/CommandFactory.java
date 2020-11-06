package cscie97.smartcity.controller;

import cscie97.smartcity.model.IoTDevice;

public interface CommandFactory {

	private Command command;

	public abstract Command createCommand(IoTDevice device);

}
