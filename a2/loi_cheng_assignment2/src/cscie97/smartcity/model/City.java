package cscie97.smartcity.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The city manages the devices of the city, its commands are accessed through a service API
 *
 * @author Loi Cheng
 * @version 1.0
 * @since 2020-09-29
 */
public class City {

    private final String cityId;
    private final String name;
    private final String blockchainAddress;
    private final Float[] location;
    private final Float radius;
    private final Map<String, IoTDevice> deviceMap;

    /**
     * City Constructor
     *
     * @param cityId   city Id
     * @param name     name of city
     * @param account  blockchain address of city
     * @param location location of city
     * @param radius   radius of city, in km
     */
    public City(String cityId, String name, String account, Float[] location, Float radius) {
        this.cityId = cityId;
        this.name = name;
        this.blockchainAddress = account;
        this.location = location;
        this.radius = radius;
        this.deviceMap = new HashMap<>();
    }

    public Float[] getLocation() {
        return this.location;
    }

    public Float getRadius() {
        return this.radius;
    }

    /**
     * To String
     *
     * @return string
     */
    @Override
    public String toString() {
        return "City{" +
                "cityId='" + cityId + '\'' +
                ", name='" + name + '\'' +
                ", blockchainAddress='" + blockchainAddress + '\'' +
                ", location=" + Arrays.toString(location) +
                ", radius=" + radius +
                " km}";
    }

    /**
     * Define Street Sign
     *
     * @param deviceId device Id
     * @param location latitude Floatitude
     * @param enabled  (true|false)
     * @param text     text to display
     * @throws ServiceException on error defining device
     */
    public void defineStreetSign(String deviceId, Float[] location, Boolean enabled, String text) throws ServiceException {
        deviceExists(deviceId);
        StreetSign sign = new StreetSign(deviceId, location, enabled, text);
        this.deviceMap.put(deviceId, sign);
    }

    /**
     * Define Info Kiosk
     *
     * @param deviceId device Id
     * @param location latitude Floatitude
     * @param enabled  (true|false)
     * @param imageUri link to image
     * @throws ServiceException on error defining device
     */
    public void defineInfoKiosk(String deviceId, Float[] location, Boolean enabled, String imageUri) throws ServiceException {
        deviceExists(deviceId);
        InfoKiosk kiosk = new InfoKiosk(deviceId, location, enabled, imageUri);
        this.deviceMap.put(deviceId, kiosk);
    }

    /**
     * Define Street Light
     *
     * @param deviceId   device Id
     * @param location   latitude Floatitude
     * @param enabled    (true|false)
     * @param brightness from 0-100
     * @throws ServiceException on error defining device
     */
    public void defineStreetLight(String deviceId, Float[] location, Boolean enabled, int brightness) throws ServiceException {
        deviceExists(deviceId);
        StreetLight light = new StreetLight(deviceId, location, enabled, brightness);
        this.deviceMap.put(deviceId, light);
    }

    /**
     * Define Parking Space
     *
     * @param deviceId device Id
     * @param location latitude Floatitude
     * @param enabled  (true|false)
     * @param rate     fee charged per hour
     * @throws ServiceException on error defining device
     */
    public void defineParkingSpace(String deviceId, Float[] location, Boolean enabled, int rate) throws ServiceException {
        deviceExists(deviceId);
        ParkingSpace space = new ParkingSpace(deviceId, location, enabled, rate);
        this.deviceMap.put(deviceId, space);
    }

    /**
     * Define Robot
     *
     * @param deviceId device Id
     * @param location latitude Floatitude
     * @param enabled  (true|false)
     * @param activity what it is doing
     * @throws ServiceException on error defining device
     */
    public void defineRobot(String deviceId, Float[] location, Boolean enabled, String activity) throws ServiceException {
        deviceExists(deviceId);
        Robot bot = new Robot(deviceId, location, enabled, activity);
        this.deviceMap.put(deviceId, bot);
    }

    /**
     * Define Vehicle
     *
     * @param deviceId device Id
     * @param location latitude Floatitude
     * @param enabled  (true|false)
     * @param type     (car|bus)
     * @param activity what it is doing
     * @param capacity seats in vehicle
     * @param fee      charged to rider per trip
     * @throws ServiceException on error defining device
     */
    public void defineVehicle(String deviceId, Float[] location, Boolean enabled, String type, String activity, int capacity, int fee) throws ServiceException {
        deviceExists(deviceId);
        VehicleType vehType;
        switch (type) {
            case "bus" -> vehType = VehicleType.bus;
            case "car" -> vehType = VehicleType.car;
            default -> throw new ServiceException("define vehicle", "type not recognized");
        }
        Vehicle vehicle = new Vehicle(deviceId, location, enabled, vehType, activity, capacity, fee);
        this.deviceMap.put(deviceId, vehicle);
    }

    /**
     * Show IoT Device
     *
     * @param deviceId device Id
     * @return IoTDevice
     * @throws ServiceException if device not found
     */
    public IoTDevice showDevice(String deviceId) throws ServiceException {
        if (!deviceMap.containsKey(deviceId)) {
            throw new ServiceException("show device", "deviceId not found!");
        }
        return this.deviceMap.get(deviceId);
    }

    /**
     * Show all devices
     *
     * @return device map
     */
    public Map<String, IoTDevice> showAllDevices() {
        return deviceMap;
    }

    /**
     * Create Sensor Event
     *
     * @param deviceId deviceId sensor is attached to
     * @param type     (microphone|camera|thermometer|co2meter|speaker)
     * @param event    what's happening with sensor
     * @param personId (optional) the person the sensor is getting info from, or sending info to
     * @throws ServiceException if device is not found
     */
    public void createSensorEvent(String deviceId, String type, String event, String personId) throws ServiceException {

        SensorType sensor;
        switch (type) {
            case "microphone" -> sensor = SensorType.microphone;
            case "camera" -> sensor = SensorType.camera;
            case "thermometer" -> sensor = SensorType.thermometer;
            case "co2meter" -> sensor = SensorType.co2meter;
            case "speaker" -> sensor = SensorType.speaker;
            default -> throw new ServiceException("create sensor activity", "unrecognized sensor type!");
        }
        // apply to all devices
        if (deviceId == null) {
            for (String key : this.deviceMap.keySet()) {
                this.deviceMap.get(key).sensorEvent(sensor, event, personId);
            }
        } else if (!this.deviceMap.containsKey(deviceId)) {
            throw new ServiceException("create sensor event", "device not found!");
        } else {
            // apply to single device
            this.deviceMap.get(deviceId).sensorEvent(sensor, event, personId);
        }
    }

    /**
     * Check if Device Already Exists
     *
     * @param deviceId the device Id
     * @throws ServiceException if already exists
     */
    private void deviceExists(String deviceId) throws ServiceException {
        if (this.deviceMap.containsKey(deviceId)) {
            throw new ServiceException("define device", "deviceId already exists!");
        }
    }
}
