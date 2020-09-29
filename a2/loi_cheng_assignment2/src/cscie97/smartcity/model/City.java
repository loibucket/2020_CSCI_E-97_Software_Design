package cscie97.smartcity.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class City {

    private String cityId;
    private String name;
    private String blockchainAddress;
    private float[] location;
    private float radius;
    private Map<String, IoTDevice> deviceMap;

    public City(String cityId, String name, String account, float[] location, float radius) {
        this.cityId = cityId;
        this.name = name;
        this.blockchainAddress = account;
        this.location = location;
        this.radius = radius;
        this.deviceMap = new HashMap<>();
    }

    @Override
    public String toString() {
        return "City{" +
                "cityId='" + cityId + '\'' +
                ", name='" + name + '\'' +
                ", blockchainAddress='" + blockchainAddress + '\'' +
                ", location=" + Arrays.toString(location) +
                ", radius=" + radius +
                '}';
    }

    public void defineStreetSign(String deviceId, float[] location, Boolean enabled, String text) throws CityException {
        deviceExists(deviceId);
        StreetSign sign = new StreetSign(deviceId, location, enabled, text);
        this.deviceMap.put(deviceId, sign);
    }

    public void defineInfoKiosk(String deviceId, float[] location, Boolean enabled, String imageUri) throws CityException {
        deviceExists(deviceId);
        InfoKiosk kiosk = new InfoKiosk(deviceId, location, enabled, imageUri);
        this.deviceMap.put(deviceId, kiosk);
    }

    public void defineStreetLight(String deviceId, float[] location, Boolean enabled, int brightness) throws CityException {
        deviceExists(deviceId);
        StreetLight light = new StreetLight(deviceId, location, enabled, brightness);
        this.deviceMap.put(deviceId, light);
    }

    public void defineParkingSpace(String deviceId, float[] location, Boolean enabled, int rate) throws CityException {
        deviceExists(deviceId);
        ParkingSpace space = new ParkingSpace(deviceId, location, enabled, rate);
        this.deviceMap.put(deviceId, space);
    }

    public void defineRobot(String deviceId, float[] location, Boolean enabled, String activity) throws CityException {
        deviceExists(deviceId);
        Robot bot = new Robot(deviceId, location, enabled, activity);
        this.deviceMap.put(deviceId, bot);
    }

    public void defineVehicle(String deviceId, float[] location, Boolean enabled, String type, String activity, int capacity, int fee) throws CityException {
        deviceExists(deviceId);
        VehicleType vehType;
        switch (type) {
            case "bus" -> vehType = VehicleType.bus;
            case "car" -> vehType = VehicleType.car;
            default -> throw new CityException("define vehicle", "type not recognized");
        }
        Vehicle vehicle = new Vehicle(deviceId, location, enabled, vehType, activity, capacity, fee);
        this.deviceMap.put(deviceId, vehicle);
    }

    public IoTDevice showDevice(String deviceId) throws CityException {
        if (!deviceMap.containsKey(deviceId)) {
            throw new CityException("show device", "deviceId not found!");
        }
        return deviceMap.get(deviceId);
    }

    public void createSensorEvent(String deviceId, String type, String event, String personId) throws CityException {
        if (!deviceMap.containsKey(deviceId)) {
            throw new CityException("create sensor activity", "deviceId not found!");
        }
        SensorType sensor;
        switch (type) {
            case "microphone" -> sensor = SensorType.microphone;
            case "camera" -> sensor = SensorType.camera;
            case "thermometer" -> sensor = SensorType.thermometer;
            case "co2meter" -> sensor = SensorType.co2meter;
            case "(speaker)" -> sensor = SensorType.speaker;
            default -> throw new CityException("create sensor activity", "unrecognized sensor type!");
        }
        deviceMap.get(deviceId).sensorEvent(sensor, event, personId);
    }

    private void deviceExists(String deviceId) throws CityException {
        if (deviceMap.containsKey(deviceId)) {
            throw new CityException("define device", "deviceId already exists!");
        }
    }

}
