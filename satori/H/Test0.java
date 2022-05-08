

import java.io.StringReader;

class Test0 {
	public static void main(String[] args) {
		Relay relay = new Relay(new StringReader("0 1 2 1 1 2 1"));
		Thread[] runners = new Thread[3];
		for (int i = 0; i < runners.length; i++) {
			runners[i] = new Thread(new AbstractRunner(i, relay) {
				@Override
				public void run() {
					while (relay.dispatch()) {
						System.out.println(id);
						synchronized (this) {
							try {
								wait(naps.nextInt(10) + 1);
							} catch (InterruptedException e) { }
						}
					}
				}
			});
		}

		for (int i = 0; i < runners.length; i++) {
			relay.register(i, runners[i]);
			runners[i].start();
		}
		relay.startRelayRace();
	}
}
