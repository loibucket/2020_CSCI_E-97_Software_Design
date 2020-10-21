package cscie97.smartcity.controller;

import cscie97.smartcity.helper.BotDist;
import cscie97.smartcity.helper.Tool;
import cscie97.smartcity.model.*;

import java.util.ArrayList;
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
     * @param c            the city
     */
    public EmergencyOneCommand(IoTDevice targetDevice, City c) {
        this.device = targetDevice;
        this.deviceMap = c.showAllDevices();
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
        Tool.report(this.device);

        //split robots
        List<BotDist> botList = Tool.getBotsByDist(this.device, this.deviceMap);
        int halfBots = botList.size() / 2;
        List<BotDist> emergencyBots = new ArrayList<>(botList.subList(0, halfBots));
        List<BotDist> shelterBots = new ArrayList<>(botList.subList(halfBots, botList.size()));

        //"address <emergency_type> at lat <lat> long <long>"
        for (BotDist b : emergencyBots) {
            Robot bot = (Robot) deviceMap.get(b.getBot());
            bot.updateRobot(this.device.getLocation(), true, "address " + event);
            Tool.report(bot);
        }

        //"help people find shelter"
        for (BotDist b : shelterBots) {
            Robot bot = (Robot) deviceMap.get(b.getBot());
            bot.updateRobot(null, true, "help people find shelter");
            Tool.report(bot);
        }

        //emergency resolved
        this.device.sensorEvent(SensorType.camera, "the " + event + " emergency is over", null);
        Tool.report(this.device);

    }
}
