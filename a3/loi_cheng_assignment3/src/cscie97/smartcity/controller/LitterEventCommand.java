package cscie97.smartcity.controller;

import cscie97.ledger.CommandProcessor;
import cscie97.ledger.CommandProcessorException;
import cscie97.smartcity.BotDist;
import cscie97.smartcity.Tool;
import cscie97.smartcity.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Command for littering events
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class LitterEventCommand implements Command {

    private final IoTDevice device;
    private final Map<String, IoTDevice> deviceMap;

    /**
     * Constructor
     *
     * @param targetDevice the reporting device
     * @param deviceMap    reference to all devices in city
     */
    public LitterEventCommand(IoTDevice targetDevice, Map<String, IoTDevice> deviceMap) {
        this.device = targetDevice;
        this.deviceMap = deviceMap;
    }

    @Override
    public void execute() throws ServiceException {

        String subject = this.device.readSensor(SensorType.camera)[1];
        //Speaker: please do not litter
        this.device.sensorEvent(SensorType.speaker, "Please do not litter!", subject);
        System.out.println(this.device);
        System.out.println(" "); // line break

        //get robots
        List<BotDist> botList = Tool.getBotsByDist(this.device, this.deviceMap);
        //get closest robot to clean
        Robot bot = (Robot) deviceMap.get(botList.get(0).getBot());
        bot.updateRobot(this.device.getLocation(), true, "cleaning garbage");
        bot.sensorEvent(SensorType.camera, "litter cleaned up", null);
        System.out.println(bot);
        System.out.println(" "); // line break


        //if resident, find blockchain address of person
        Person person = CommandAPI.getRegistry().showPerson(subject);
        if (person.getType() == PersonType.resident) {
            String address = person.getBlockchainAddress();
            try {
                CommandProcessor.processCommand("get-account-balance " + address, -1);
                CommandProcessor.processCommand("process-transaction 1 amount 50 fee 10 note \"littering\" payer " + address + " receiver master", -1);
                CommandProcessor.processCommand("get-account-balance " + address, -1);
            } catch (CommandProcessorException e) {
                //print ledger processing errors
                System.out.println(e.toString());
            }
        }

//        try {
//            //do ledger things
//            CommandProcessor.processCommand("ok", -99);
//        } catch (CommandProcessorException e) {
//            //print ledger processing errors
//            System.out.println(e.toString());
//        }

        //do registry things
        //CommandAPI.processCommand("placeholder", "ok", -99);


    }
}
