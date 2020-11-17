package cscie97.smartcity.controller;

import cscie97.ledger.LedgerApi;
import cscie97.smartcity.authenticator.*;
import cscie97.smartcity.model.*;

/**
 * Command for bus boarding
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class BoardBusCommand implements Command {

    private final IoTDevice device;
    private final String cityBlockchain;

    /**
     * constructor
     *
     * @param targetDevice the device that is being boarded
     * @param c            the city
     */
    public BoardBusCommand(IoTDevice targetDevice, City c) {
        this.device = targetDevice;
        this.cityBlockchain = c.getBlockchainAddress();
    }

    /**
     * Execute, greets boarder, charge fee if resident
     *
     * @throws ServiceException if error in greeting or charging
     */
    @Override
    public void execute() throws ServiceException, AuthException {

        // bus
        System.out.println(this.device);
        System.out.println(" "); // line break

        String personId = this.device.readSensor(SensorType.camera)[1];

        Person person;
        try {
            person = ModelApi.getRegistry().showPerson(personId);
            System.out.println(person);
            System.out.println(" "); // line break
        } catch (Exception e) {
            throw new ServiceException("board bus", "person id not found!");
        }

        // charge resident
        try {
            Vehicle bus = (Vehicle) this.device;
            if (person.getType() == PersonType.resident) {
                LedgerApi.processCommand("get-account-balance " + person.getBlockchainAddress(), -1);
                LedgerApi.processCommand("process-transaction 1 amount " + bus.getFee() + " fee 10 note \"board bus\" payer " + person.getBlockchainAddress() + " receiver " + cityBlockchain, -1);
                LedgerApi.processCommand("get-account-balance " + person.getBlockchainAddress(), -1);
            }
        } catch (Exception e) {
            throw new ServiceException("board bus", "ledger transaction error!");
        }

        // bus boarded
        this.device.sensorEvent(SensorType.camera, "bus boarded", personId);
        this.device.sensorEvent(SensorType.speaker, "hello, good to see you " + person.getName(), personId);
        System.out.println(this.device);
        System.out.println(" "); // line break
    }
}
