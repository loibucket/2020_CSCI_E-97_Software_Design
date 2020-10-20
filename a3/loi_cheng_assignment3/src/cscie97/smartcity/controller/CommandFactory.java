package cscie97.smartcity.controller;

import cscie97.smartcity.model.IoTDevice;
import cscie97.smartcity.model.ServiceException;

import java.util.List;

public interface CommandFactory {

	Command createCommand() throws ServiceException;

}
