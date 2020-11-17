package cscie97.smartcity.controller;

import cscie97.smartcity.authenticator.AuthException;
import cscie97.smartcity.authenticator.AuthToken;
import cscie97.smartcity.model.*;
import cscie97.smartcity.model.IoTObserver;

/**
 * The camera controller observes events from the camera and responds accordingly
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class CameraController implements IoTObserver, CommandFactory {

    private final City city;
    private String command;
    private IoTDevice targetDevice;

    public CameraController(City c) {
        this.city = c;
    }

    /**
     * Create a command to be executed later, based on info stored in the camera controller's command var
     *
     * @return the command
     * @throws ServiceException if error in creating the command
     */
    @Override
    public Command createCommand() throws ServiceException, AuthException {
        Command c;
        switch (this.command) {
            case "emergency_one" -> c = new EmergencyOneCommand(this.targetDevice, this.city);
            case "emergency_two" -> c = new EmergencyTwoCommand(this.targetDevice, this.city);
            case "littering" -> c = new LitterEventCommand(this.targetDevice, this.city);
            case "person_seen" -> c = new PersonSeenCommand(this.targetDevice, this.city);
            case "board_bus" -> c = new BoardBusCommand(this.targetDevice, this.city);
            default -> throw new ServiceException("camera controller", "command not found!");
        }
        return c;
    }

    /**
     * Observe for camera events from the passed in devices, responds on specific events, executes a specific command
     *
     * @param d device to observe
     * @throws ServiceException if observation errors
     */
    @Override
    public void observe(IoTDevice d) throws ServiceException, AuthException {

        if (d == null) {
            throw new ServiceException("camera controller", "no devices to observe!");
        }

        // focus only on camera events
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
                case "Person boards bus" -> {
                    this.command = "board_bus";
                    this.targetDevice = d;
                }
                default -> {
                    this.command = "nothing to see here";
                    // no action taken
                    return;
                }
            }
            //create and execute command
            System.out.println("-CAMERA CONTROLLER ACTIVATED-");
            createCommand().execute();

        }
    }//observe

}//class
