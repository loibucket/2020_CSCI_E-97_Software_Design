package cscie97.smartcity.controller;

import cscie97.smartcity.authenticator.*;
import cscie97.smartcity.shared.BotDist;
import cscie97.smartcity.shared.Tool;
import cscie97.smartcity.model.*;

import java.util.ArrayList;
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
     * @param c the city
     */
    public EmergencyTwoCommand(IoTDevice d, City c) throws AuthException {
        this.device = d;
        this.deviceMap = c.showAllDevices();
    }

    /**
     * The action to be executed
     *
     * @throws ServiceException if error in building the command
     */
    @Override
    public void execute() throws ServiceException, AuthException {

        //stay calm
        this.device.sensorEvent(SensorType.speaker, "Stay calm, help is on its way!", null);
        Tool.report(this.device);

        //get robots
        List<BotDist> botList = Tool.getBotsByDist(this.device, this.deviceMap);
        List<BotDist> two = new ArrayList<>(botList.subList(0, 2));

        //"address <emergency_type> at lat <lat> long <long>"
        for (BotDist b : two) {
            Robot bot = (Robot) deviceMap.get(b.getBot());
            bot.updateRobot(this.device.getLocation(), true, "address traffic accident");
            bot.sensorEvent(SensorType.speaker, "Help is here, stay calm", this.device.readSensor(SensorType.camera)[1]);
            Tool.report(bot);
        }

        //clear emergency from camera
        this.device.sensorEvent(SensorType.camera, "accident cleared", null);
        Tool.report(this.device);

    }
}
