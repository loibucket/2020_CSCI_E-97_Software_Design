package cscie97.ledger;

public interface Processor {

	public abstract void processCommand(String authToken, String command);

}
