package cscie97.smartcity.controller;

import cscie97.smartcity.BotDist;
import cscie97.smartcity.Tool;
import cscie97.smartcity.model.*;

import java.util.*;

public class CameraController implements IoTObserver, CommandFactory {

	private EmergencyOneCommand emergencyOne;

	private EmergencyTwoCommand emergencyTwo;

	private LitterEventCommand litterEvent;

	private PersonSeenCommand personSeen;

	private BoardBusCommand boardBus;

	@Override
	public Command createCommand(List<IoTDevice> deviceList) {
		return null;
	}

	@Override
	public void observe(List<IoTDevice> deviceList, Map<String, IoTDevice> deviceMap) throws ServiceException {

		// focus only on camera events
		for(IoTDevice device:deviceList){
			String action = device.readSensor(SensorType.camera)[0];
			String subject = device.readSensor(SensorType.camera)[1];

			if (action != null){

				//check for emergency
				if (action.startsWith("emergency")){
					String situation = action.substring(10,action.length());
					List<String> latLong;
					Float[] location;

					//traffic accident
					if (situation.startsWith("traffic_accident")) {
						try {
							latLong = Arrays.asList(situation.substring(17, situation.length()).split(" "));
							float lat = Float.parseFloat(Objects.requireNonNull(Tool.findAttr(latLong, "lat")));
							float lon = Float.parseFloat(Objects.requireNonNull(Tool.findAttr(latLong, "long")));
							location = new Float[]{lat,lon};
						}catch (Exception e){
							throw new ServiceException("traffic accident","location not understood!");
						}

						///COMMAND///
						//stay calm
						device.sensorEvent(SensorType.speaker,"Stay calm, help is on its way!",null);
						System.out.println(device);
						System.out.println(" "); // line break

						//get robots
						List<BotDist> bots = new ArrayList<>();
						for(String botId:deviceMap.keySet()){
							IoTDevice d = deviceMap.get(botId);
							if (d.getClass().getName().equals("cscie97.smartcity.model.Robot")){
								bots.add(new BotDist(botId,Tool.distance(location, d.getLocation())));
							}
						}

						//get closest robot
						Collections.sort(bots);
						List<BotDist> two = new ArrayList<BotDist>(bots.subList(0, 2));

						//"address <emergency_type> at lat <lat> long <long>"
						for (BotDist b: two){
							Robot bot = (Robot)deviceMap.get(b.getBot());
							bot.updateRobot(location,true,"address traffic accident");
							bot.sensorEvent(SensorType.speaker,"I am addressing emergency",subject);
							System.out.println(bot);
							System.out.println(" "); // line break
						}
						///COMMAND////
					}

//					if (situation.startsWith("fire")){
//						latLong = situation.substring(5,situation.length());
//					} else if (situation.startsWith("flood")){
//						latLong = situation.substring(6, situation.length());
//					} else if (situation.startsWith("earthquake")){
//						latLong = situation.substring(11,situation.length());
//					} else if (situation.startsWith("severe weather")){
//						latLong = situation.substring(15,situation.length());
//					}
//
//					System.out.println("CAMERA CONTROLLER: " + "non critical emergency detected, no action taken");
//					System.out.println("CAMERA CONTROLLER: " + action);
				}
			}
		}
	}

	/**
	 * Helper: get attribute from command
	 *
	 * @param a    command
	 * @param attr attribute name
	 * @return attribute value
	 */
	private String findLatLong(List<String> a, String attr) {
		int Idx = a.indexOf(attr);
		if (Idx == -1) {
			return null;
		} else if (a.size() > Idx + 1) {
			return a.get(Idx + 1);
		} else {
			return null;
		}
	}
}
