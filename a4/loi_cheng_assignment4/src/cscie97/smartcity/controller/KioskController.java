package cscie97.smartcity.controller;

import cscie97.smartcity.authenticator.AuthException;
import cscie97.smartcity.authenticator.AuthToken;
import cscie97.smartcity.model.*;
import cscie97.smartcity.model.IoTObserver;

/**
 * Control all the kiosks in the city
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-10-19
 */
public class KioskController implements IoTObserver, CommandFactory {

    private final City city;
    private IoTDevice kiosk;
    private String command;

    /**
     * constructor
     *
     * @param city the city being controlled
     */
    public KioskController(City city) {
        this.city = city;
    }

    /**
     * create command to answer movie questions
     *
     * @return command to execute
     */
    @Override
    public Command createCommand() throws ServiceException, AuthException {

        switch (this.command) {
            case "movie_info" -> {
                return new MovieInfoCommand(this.kiosk, this.city);
            }
            case "movie_reservation" -> {
                return new MovieReservationCommand(this.kiosk, this.city);
            }
            default -> throw new ServiceException("kiosk controller", "command not found!");
        }
    }

    /**
     * check kiosks for requests
     *
     * @param d the device the city spammed you
     */
    @Override
    public void observe(IoTDevice d) throws ServiceException, AuthException {
        this.kiosk = d;
        //check if device given is a kiosk
        if (this.kiosk.getClass().getName().equals("cscie97.smartcity.model.InfoKiosk")) {

            //only respond to relevant requests
            String request = this.kiosk.readSensor(SensorType.microphone)[0];

            if (request == null) {
                return;
            }

            if (request.contains("what movies are showing tonight?")) {
                this.command = "movie_info";
                System.out.println("-KIOSK CONTROLLER ACTIVATED-");
                createCommand().execute();
            } else if (request.contains("reserve") && request.contains("seats for the") && request.contains("showing of")) {
                this.command = "movie_reservation";
                System.out.println("-KIOSK CONTROLLER ACTIVATED-");
                createCommand().execute();
            }
        }
    }
}
