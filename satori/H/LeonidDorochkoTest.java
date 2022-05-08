import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LeonidDorochkoTest {
    static class OurRunnable implements Runnable {
        int id;
        StringBuilder answer;
        Relay relay;

        OurRunnable(int id, StringBuilder answer, Relay relay) {
            this.id = id;
            this.answer = answer;
            this.relay = relay;
        }

        @Override
        public void run() {
            while (relay.dispatch()) {
                synchronized (this) {
                    answer.append(Integer.toString(id));
                    try {
                        wait(100);
                    } catch (InterruptedException e) { }
                }
            }
        }
    }

    @Test
    public void sleep_before_starting_race() {
        Relay relay = new Relay(new StringReader("2 1 1 2 2 1"));
        StringBuilder answer = new StringBuilder();

        Thread first = new Thread(new OurRunnable(1, answer, relay));
        Thread second = new Thread(new OurRunnable(2, answer, relay));

        relay.register(1, first);
        relay.register(2, second);

        first.start();
        second.start();

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        relay.startRelayRace();

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("211221", answer.toString());
        // if you have empty string, that means that threads are in wait state
    }
}
