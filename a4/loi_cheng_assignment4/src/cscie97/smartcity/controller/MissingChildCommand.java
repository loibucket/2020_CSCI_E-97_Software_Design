package cscie97.smartcity.controller;

import cscie97.smartcity.shared.*;
import cscie97.smartcity.model.*;

import java.util.List;
import java.util.Map;

/**
 * A series of actions to execute on a missing child event
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class MissingChildCommand implements Command {

    private String childId;
    private City city;
    private IoTDevice device;
    private Map<String, IoTDevice> deviceMap;
    private Person child;
    private Person adult;

    /**
     * Constructor
     *
     * @param device the device that heard the event
     * @param city   the city where it happened
     */
    public MissingChildCommand(IoTDevice device, City city) {
        this.city = city;
        this.device = device;
        this.deviceMap = city.showAllDevices();
    }

    /**
     * Attempt to find the missing child and bring back to adult
     *
     * @throws ServiceException if any error
     */
    @Override
    public void execute() throws ServiceException {
        //the device that heard the event
        Tool.report(this.device);

        //update adult location to device
        this.adult = ModelAPI.getRegistry().showPerson(this.device.readSensor(SensorType.microphone)[1]);
        this.adult.updateLocation(this.device.getLocation());
        Tool.report(this.adult);

        //get child id
        String sound = this.device.readSensor(SensorType.microphone)[0];
        String[] words = sound.split(" ");

        //get child location
        this.childId = words[words.length - 1];
        this.child = ModelAPI.getRegistry().showPerson(this.childId);
        Float[] childLocation = this.child.getLocation();

        //update speaker to inform adult
        this.device.sensorEvent(SensorType.speaker, "person " + childId + " is at lat " + childLocation[0] + " long " + childLocation[1] +
                ", a robot is retrieving now, stay where you are.", this.device.readSensor(SensorType.microphone)[1]);
        Tool.report(this.device);

        //get robots
        List<BotDist> botList = Tool.getBotsByDist(childLocation, this.deviceMap);
        //get closest robot to help
        Robot bot = (Robot) deviceMap.get(botList.get(0).getBot());
        bot.updateRobot(childLocation, true, "found child");
        Tool.report(bot);

        //bring bot back to adult
        bot.updateRobot(this.adult.getLocation(), true, "retrieved child");
        Tool.report(bot);

        //update device
        this.device.sensorEvent(SensorType.microphone, "I was so worried about you!", this.device.readSensor(SensorType.microphone)[1]);
    }
}
