package cscie97.smartcity.controller;

import cscie97.smartcity.BotDist;
import cscie97.smartcity.Tool;
import cscie97.smartcity.model.IoTDevice;
import cscie97.smartcity.model.Robot;
import cscie97.smartcity.model.SensorType;
import cscie97.smartcity.model.ServiceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Emergency One command addresses fire, flood, earthquake, and severe weather seen on camera
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */

public class EmergencyOneCommand implements Command {

    private final IoTDevice device;
    private final Map<String, IoTDevice> deviceMap;

    /**
     * Constructor
     *
     * @param targetDevice the reporting device
     * @param deviceMap    reference to all devices in city
     */
    public EmergencyOneCommand(IoTDevice targetDevice, Map<String, IoTDevice> deviceMap) {
        this.device = targetDevice;
        this.deviceMap = deviceMap;
    }

    /**
     * The action to be executed
     *
     * @throws ServiceException if error in building the command
     */
    @Override
    public void execute() throws ServiceException {

        String event = device.readSensor(SensorType.camera)[0];

        // announce over entire city, all device speakers
        for (String key : deviceMap.keySet()) {
            deviceMap.get(key).sensorEvent(SensorType.speaker, "Alert! " + event + "! please find shelter immediately!", null);
        }
        System.out.println(this.device);
        System.out.println(" "); // line break

        //split robots
        List<BotDist> botList = Tool.getBotsByDist(this.device, this.deviceMap);
        int halfBots = botList.size() / 2;
        List<BotDist> emergencyBots = new ArrayList<>(botList.subList(0, halfBots));
        List<BotDist> shelterBots = new ArrayList<>(botList.subList(halfBots, botList.size()));

        //"address <emergency_type> at lat <lat> long <long>"
        for (BotDist b : emergencyBots) {
            Robot bot = (Robot) deviceMap.get(b.getBot());
            bot.updateRobot(this.device.getLocation(), true, "address " + event);
            System.out.println(bot);
            System.out.println(" "); // line break
        }

        //"help people find shelter"
        for (BotDist b : shelterBots) {
            Robot bot = (Robot) deviceMap.get(b.getBot());
            bot.updateRobot(null, true, "help people find shelter");
            System.out.println(bot);
            System.out.println(" "); // line break
        }

        //emergency resolved
        this.device.sensorEvent(SensorType.camera, "the " + event + " emergency is over", null);
        System.out.println(this.device);
        System.out.println(" "); // line break

    }
}
