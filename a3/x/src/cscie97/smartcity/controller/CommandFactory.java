package cscie97.smartcity.controller;

public interface CommandFactory {

	private Command command;

	public abstract Command createCommand(List<IoTDevice> deviceList);

}
