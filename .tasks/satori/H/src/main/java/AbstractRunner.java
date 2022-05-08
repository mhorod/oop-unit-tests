

import java.util.Random;

public abstract class AbstractRunner implements Runnable {

	final protected Random naps = new Random();
	final protected int id;
	final protected Relay relay;

	public AbstractRunner(int id, Relay relay) {
		this.id = id;
		this.relay = relay;
	}
}
