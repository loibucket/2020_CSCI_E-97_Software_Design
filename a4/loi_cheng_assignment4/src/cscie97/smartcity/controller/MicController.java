package cscie97.smartcity.controller;

import cscie97.smartcity.model.*;
import cscie97.smartcity.model.IoTObserver;

/**
 * The mic controller observes events from the microphone and responds accordingly
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class MicController implements IoTObserver, CommandFactory {

    private final City city;
    private String command;
    private IoTDevice device;

    /**
     * Constructor
     *
     * @param c the city
     */
    public MicController(City c) {
        this.city = c;
    }

    /**
     * create the command
     *
     * @return the command
     * @throws ServiceException if any errors in making the command
     */
    @Override
    public Command createCommand() throws ServiceException {

        Command c = null;
        switch (this.command) {
            case "missing_child" -> {
                c = new MissingChildCommand(this.device, this.city);
            }
            case "broken_glass" -> {
                c = new BrokenGlassCommand(this.device, this.city);
            }
            case "bus_route" -> {
                c = new BusRouteCommand(this.device, this.city);
            }
            default -> throw new ServiceException("mic controller", "command not found!");
        }
        return c;
    }

    /**
     * Observe device and execute command if necessary
     *
     * @param device the device check events from
     * @throws ServiceException if observation error
     */
    @Override
    public void observe(IoTDevice device) throws ServiceException {

        this.device = device;
        String sound = this.device.readSensor(SensorType.microphone)[0];

        if (sound == null) {
            return;
        }

        if (sound.startsWith("can you help me find my child")) {
            this.command = "missing_child";
            createCommand().execute();
        } else if (sound.equals("broken_glass_sound")) {
            this.command = "broken_glass";
            createCommand().execute();
        } else if (sound.startsWith("Does this bus go to ")) {
            this.command = "bus_route";
            createCommand().execute();
        }

    }

}
