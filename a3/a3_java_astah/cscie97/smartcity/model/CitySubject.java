package cscie97.smartcity.model;

public interface CitySubject {

	private List<IoTObserver> observers;

	public abstract void attach(IoTObserver observer);

	public abstract void detach(IoTObserver observer);

	public abstract void notify();

}
