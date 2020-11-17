package cscie97.smartcity.controller;

import cscie97.ledger.LedgerApi;
import cscie97.smartcity.authenticator.AuthException;
import cscie97.smartcity.shared.*;
import cscie97.smartcity.model.*;

import java.util.Objects;

/**
 * Command for parking event
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class ParkingEventCommand implements Command {

    private final ParkingSpace space;
    private final String cityBlockchain;

    /**
     * constructor
     *
     * @param targetSpace the parking space
     * @param city        the city the space is in
     */
    public ParkingEventCommand(IoTDevice targetSpace, City city) {
        this.space = (ParkingSpace) targetSpace;
        this.cityBlockchain = city.getBlockchainAddress();
    }

    /**
     * charge the vehicle for parking
     */
    @Override
    public void execute() throws ServiceException, AuthException {

        //the parking space
        Tool.report(this.space);

        //the camera footage
        String footage = this.space.readSensor(SensorType.camera)[0];

        //try to get the vehicle id
        String vehicleId;
        try {
            vehicleId = Objects.requireNonNull(Tool.findAttr(footage, "Vehicle"));
        } catch (Exception e) {
            throw new ServiceException("parking command", "vehicle id not found!");
        }

        //try to get how long the vehicle has parked
        int duration;
        try {
            duration = Integer.parseInt(Objects.requireNonNull(Tool.findAttr(footage, "for")));
        } catch (Exception e) {
            throw new ServiceException("parking command", "parking duration not found!");
        }

        //amount to charge
        int charge = this.space.getRate() * duration;

        //open ledger and charge vehicle
        try {
            LedgerApi.processCommand("get-account-balance " + vehicleId, -1);
            LedgerApi.processCommand("process-transaction 1 amount " + charge + " fee 10 note \"parking\" payer " +
                    vehicleId + " receiver " + this.cityBlockchain, -1);
            LedgerApi.processCommand("get-account-balance " + vehicleId, -1);
        } catch (Exception e) {
            //print ledger processing errors
            System.out.println(e.toString());
        }

        //update the parking space
        this.space.sensorEvent(SensorType.camera, "vehicle just left", null);
        Tool.report(this.space);

    }
}
