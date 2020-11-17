package cscie97.smartcity.controller;

import cscie97.smartcity.authenticator.*;
import cscie97.smartcity.shared.BotDist;
import cscie97.smartcity.shared.Tool;
import cscie97.smartcity.model.City;
import cscie97.smartcity.model.IoTDevice;
import cscie97.smartcity.model.Robot;
import cscie97.smartcity.model.SensorType;

import java.util.List;

/**
 * Command for broken glass
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class BrokenGlassCommand implements Command {

    private final IoTDevice device;
    private final City city;

    /**
     * Constructor
     *
     * @param device the device that heard the broken glass
     * @param city   the city where it happened
     */
    public BrokenGlassCommand(IoTDevice device, City city) {
        this.device = device;
        this.city = city;
    }

    /**
     * Send the closest robot to clean the mess
     */
    @Override
    public void execute() throws AuthException {
        //the device
        Tool.report(this.device);

        //get closest robot to clean
        List<BotDist> botList = Tool.getBotsByDist(this.device, city.showAllDevices());
        Robot bot = (Robot) city.showAllDevices().get(botList.get(0).getBot());
        bot.updateRobot(this.device.getLocation(), true, "cleaning broken glass");
        Tool.report(bot);

        //update device sensor
        this.device.sensorEvent(SensorType.microphone, "sound of robot cleaning up broken glass", null);
        Tool.report(this.device);
    }
}
