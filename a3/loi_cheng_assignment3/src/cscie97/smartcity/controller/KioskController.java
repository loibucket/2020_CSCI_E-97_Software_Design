package cscie97.smartcity.controller;

import cscie97.smartcity.model.IoTDevice;
import cscie97.smartcity.model.IoTObserver;

import java.util.List;
import java.util.Map;

public class KioskController implements IoTObserver, CommandFactory {

    private MovieInfoCommand movieInfo;

    private MovieReservationCommand movieReservation;

    @Override
    public Command createCommand() {
        return null;
    }

    @Override
    public void observe(IoTDevice d) {
        //System.out.println("kioskcontroller:"+deviceList.toString());
    }
}
