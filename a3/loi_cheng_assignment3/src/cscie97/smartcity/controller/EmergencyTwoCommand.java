package cscie97.smartcity.controller;

import cscie97.smartcity.BotDist;
import cscie97.smartcity.Tool;
import cscie97.smartcity.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Emergency Two command addresses traffic accidents caught on camera
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class EmergencyTwoCommand implements Command {

    private final IoTDevice device;
    private final Map<String, IoTDevice> deviceMap;


    /**
     * Constructor
     *
     * @param targetDevice the reporting device
     * @param c            the city
     */
    public EmergencyTwoCommand(IoTDevice d, City c) {
        this.device = d;
        this.deviceMap = c.showAllDevices();
    }

    /**
     * The action to be executed
     *
     * @throws ServiceException if error in building the command
     */
    @Override
    public void execute() throws ServiceException {

        //stay calm
        this.device.sensorEvent(SensorType.speaker, "Stay calm, help is on its way!", null);
        System.out.println(this.device);
        System.out.println(" "); // line break

        //get robots
        List<BotDist> botList = Tool.getBotsByDist(this.device, this.deviceMap);
        List<BotDist> two = new ArrayList<>(botList.subList(0, 2));

        //"address <emergency_type> at lat <lat> long <long>"
        for (BotDist b : two) {
            Robot bot = (Robot) deviceMap.get(b.getBot());
            bot.updateRobot(this.device.getLocation(), true, "address traffic accident");
            bot.sensorEvent(SensorType.speaker, "Help is here, stay calm", this.device.readSensor(SensorType.camera)[1]);
            System.out.println(bot);
            System.out.println(" "); // line break
        }

        //clear emergency from camera
        this.device.sensorEvent(SensorType.camera, "traffic accident cleared", null);
        System.out.println(this.device);
        System.out.println(" "); // line break

    }
}
