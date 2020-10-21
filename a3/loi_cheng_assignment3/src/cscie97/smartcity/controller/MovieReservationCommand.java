package cscie97.smartcity.controller;

import cscie97.ledger.CommandProcessor;
import cscie97.ledger.CommandProcessorException;
import cscie97.smartcity.helper.Tool;
import cscie97.smartcity.model.*;

import java.util.Objects;

/**
 * Command to make a reservation
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class MovieReservationCommand implements Command {

    private static final int TICKET_PRICE = 5;

    private final InfoKiosk kiosk;
    private final String cityBlockchain;

    /**
     * constructor
     *
     * @param kiosk taking requests
     */
    public MovieReservationCommand(IoTDevice kiosk, City city) {
        this.kiosk = (InfoKiosk) kiosk;
        this.cityBlockchain = city.getBlockchainAddress();
    }

    /**
     * Make the reservation
     *
     * @throws ServiceException if any errors
     */
    @Override
    public void execute() throws ServiceException {

        //the kiosk
        Tool.report(this.kiosk);

        //lookup person
        Person person;
        String personId;
        try {
            personId = this.kiosk.readSensor(SensorType.microphone)[1];
            person = CommandAPI.getRegistry().showPerson(personId);
            Tool.report(person);
        } catch (Exception e) {
            throw new ServiceException("movie reservation command", "person id not found!");
        }

        //charge only if resident, free for visitors
        if (person.getType() == PersonType.resident) {

            //calculate charges
            String request = this.kiosk.readSensor(SensorType.microphone)[0];
            int seats;
            int charge;
            try {
                seats = Integer.parseInt(Objects.requireNonNull(Tool.findAttr(request, "reserve")));
                charge = seats * TICKET_PRICE;
            } catch (Exception e) {
                throw new ServiceException("movie reservation command", "number of seats unclear!");
            }

            //open ledger and charge person
            try {
                CommandProcessor.processCommand("get-account-balance " + person.getBlockchainAddress(), -1);
                CommandProcessor.processCommand("process-transaction 1 amount " + charge + " fee 10 note \"movie reservation\" payer " +
                        person.getBlockchainAddress() + " receiver " + this.cityBlockchain, -1);
                CommandProcessor.processCommand("get-account-balance " + person.getBlockchainAddress(), -1);
            } catch (CommandProcessorException e) {
                //print ledger processing errors
                System.out.println(e.toString());
            }
        }

        //update kiosk
        this.kiosk.sensorEvent(SensorType.speaker, "your seats are reserved; please arrive a few minutes early!", this.kiosk.readSensor(SensorType.microphone)[1]);
        this.kiosk.sensorEvent(SensorType.microphone, "your seats are reserved; please arrive a few minutes early!", null);
        Tool.report(kiosk);
    }
}
