package cscie97.smartcity.controller;

import cscie97.ledger.CommandProcessorException;
import cscie97.smartcity.model.*;

import java.util.*;

/**
 * The camera controller observes events from the camera and responds accordingly
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class CameraController implements IoTObserver, CommandFactory {

    private EmergencyOneCommand emergencyOne;

    private EmergencyTwoCommand emergencyTwo;

    private LitterEventCommand litterEvent;

    private PersonSeenCommand personSeen;

    private BoardBusCommand boardBus;

    private IoTDevice targetDevice;

    private List<IoTDevice> deviceList;

    private Map<String, IoTDevice> deviceMap;

    private String command;

    /**
     * Create a command to be executed later, based on info stored in the camera controller's command var
     *
     * @return the command
     * @throws ServiceException if error in creating the command
     */
    @Override
    public Command createCommand() throws ServiceException {
        Command c;
        switch (this.command) {
            case "emergency_one" -> c = new EmergencyOneCommand(targetDevice, deviceMap);
            case "emergency_two" -> c = new EmergencyTwoCommand(targetDevice, deviceMap);
            case "littering" -> c = new LitterEventCommand(targetDevice, deviceMap);
            default -> throw new ServiceException("camera controller", "cannot create command!");
        }
        return c;
    }

    /**
     * Observe for camera events from the passed in devices, responds on specific events, executes a specific command
     *
     * @param deviceList list of devices to observe
     * @param deviceMap  reference of all the devices in the city
     * @throws ServiceException if observation errors
     */
    @Override
    public void observe(List<IoTDevice> deviceList, Map<String, IoTDevice> deviceMap) throws ServiceException {

        if (deviceList == null) {
            throw new ServiceException("camera controller", "no devices to observe!");
        }

        this.deviceList = deviceList;
        this.deviceMap = deviceMap;

        // focus only on camera events
        for (IoTDevice d : deviceList) {
            String action;
            action = d.readSensor(SensorType.camera)[0];

            // inspect camera footage
            if (action != null) {
                switch (action) {
                    case "fire", "flood", "earthquake", "severe weather" -> {
                        this.command = "emergency_one";
                        this.targetDevice = d;
                    }
                    case "traffic_accident" -> {
                        this.command = "emergency_two";
                        this.targetDevice = d;
                    }
                    case "littering" -> {
                        this.command = "littering";
                        this.targetDevice = d;
                    }
                    case "person_seen" -> {
                        this.command = "person_seen";
                        this.targetDevice = d;
                    }
                    case "board_bus" -> {
                        this.command = "board_bus";
                        this.targetDevice = d;
                    }
                    default -> {
                        // no action taken
                    }
                }
                //create and execute command
                createCommand().execute();
            }
        }
    }//observe

}//class
