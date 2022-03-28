import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;


@RunWith(Enclosed.class)
public class MichalHorodeckiAgentTest
{
    public static final String allCharacters =
            "abcdefghijklmnopqrstuvwxyz" + "0123456789" + "ąćęłńóśżź" + ".,!?/";

    // Test if agent receives transmissions properly
    public static class ReceivingTest
    {
        @Test
        public void every_character_can_be_encoded_and_then_decoded()
        {
            // Test if all characters at once are transmittable
            var textTransmitter = new Agent(allCharacters);
            var textReceiver = new Agent(textTransmitter);
            var textScanner = new Scanner(textReceiver);
            assertEquals(allCharacters, textScanner.next());
        }

        @Test
        public void each_character_can_be_encoded_and_decoded_separately()
        {
            // Test transmitting a single character
            // Checking all letters to check potential edge cases
            for (char letter : allCharacters.toCharArray())
            {
                var letterTransmitter = new Agent("" + letter);
                var letterReceiver = new Agent(letterTransmitter);
                var letterScanner = new Scanner(letterReceiver);
                assertEquals("" + letter, letterScanner.next());
                assertFalse(letterScanner.hasNext());
            }
        }

        @Test
        @Ignore // This test isn't checked by satori
        public void read_consumes_text_to_word_delimiter()
        {
            // Test if Agent consumes text to word delimiter even if the scanner
            // uses letter as a delimiter
            var textTransmitter = new Agent("firstxcde/secondxcde/foo");

            Scanner s1 = new Scanner(new Agent(textTransmitter));
            Scanner s2 = new Scanner(new Agent(textTransmitter));

            s1.useDelimiter("x");
            s2.useDelimiter("x");

            // s1.read() should consume whole word
            assertEquals("first", s1.next());
            // s2.read() should start reading from the next word
            assertEquals("second", s2.next());
            // s1.read() returns the rest of first word and consumes everything
            assertEquals("cde/foo", s1.next());
        }

        @Test
        public void agent_does_not_request_data_after_transmission_end()
        {
            String[] transmission = {".", "-", "stop"};

            var transmitter = new MockTransmitter(transmission);
            var receiver = new Scanner(new Agent(transmitter));
            receiver.next(); // consume the letter
            // hasNext calls `read` which should *not* call transmituj anymore
            assertFalse(receiver.hasNext());
        }

        @Test
        public void rozmowa_kontrolowana_is_ignored()
        {
            String[] transmission = {".", "kontrola", "-", "kontrola", "kontrola", "stop", ".", "-", "stop", "kontrola"};
            String expected = "aa";

            var transmitter = new MockTransmitter(transmission);
            var receiver = new Scanner(new Agent(transmitter));
            assertEquals(expected, receiver.next());
        }
    }

    // Test if agent transmits data properly
    public static class TransmissionTest
    {
        @Test
        public void message_ends_with_koniec()
        {
            var agent = new Agent("aaa");
            var receiver = new MockReceiver();
            receiver.receiveN(agent, 9); // receive letters and stops
            assertEquals("koniec", receiver.receiveOne(agent));
        }

        @Test
        public void character_ends_with_stop()
        {
            var agent = new Agent("aaa");
            var receiver = new MockReceiver();
            for (int i = 0; i < 3; i++)
            {
                receiver.receiveN(agent, 2); // a = .-
                assertEquals("stop", receiver.receiveOne(agent));
            }
        }

        @Test
        @Ignore // This test isn't checked by satori
        public void empty_message_is_just_koniec()
        {
            var emptyAgent = new Agent("");
            var receiver = new MockReceiver();
            assertEquals("koniec", receiver.receiveOne(emptyAgent));
        }

        @Test
        public void test_transmituj_does_not_listen_to_transmission()
        {
            // Test if transmitter does not consume data from agent it listens to

            var innerAgent = new Agent("foo/bar/baz/");

            // Testing behavior of those agents
            // We are connecting two agents to inner to make sure no caching occurs
            var firstInnerListener = new Agent(innerAgent);
            var secondInnerListener = new Agent(innerAgent);

            var outerAgent = new Agent(firstInnerListener);
            var outerScanner = new Scanner(outerAgent);

            var secondInnerListenerScanner = new Scanner(secondInnerListener);

            outerScanner.useDelimiter("/");
            secondInnerListenerScanner.useDelimiter("/");

            outerScanner.next(); // outer.read() -> firstInnerListener.transmituj()
            assertEquals("foo", secondInnerListenerScanner.next());
        }

        @Test
        public void read_does_not_advance_transmission()
        {
            var innerAgent = new Agent();
            var agent = new Agent(innerAgent, "foo/bar/baz/");
            // Listens to agent transmission
            var agentListener = new Scanner(new Agent(agent));
            // Calls read
            var agentReader = new Scanner(agent);
            agentListener.useDelimiter("/");
            agentReader.useDelimiter("/");

            agentReader.next();
            assertEquals("foo", agentListener.next());
            agentReader.next();
            agentReader.next();
            assertEquals("bar", agentListener.next());
        }

        @Test
        public void transmission_state_is_independent_of_listeners()
        {
            var transmitter = new Agent("a/b/c/d/e/");
            Scanner[] listeners = {null, null, null};
            for (int i = 0; i < 3; i++)
            {
                listeners[i] = new Scanner(new Agent(transmitter));
                listeners[i].useDelimiter("/");
            }

            // Order of listeners and calls doesn't really matter
            // All that is important is that different listeners are called
            // throughout the test
            assertEquals("a", listeners[1].next());
            assertEquals("b", listeners[0].next());
            assertEquals("c", listeners[0].next());
            assertEquals("d", listeners[2].next());
            assertEquals("e", listeners[1].next());
        }
    }

    // Test everything related to this little mf
    public static class ZuberTest
    {
        @Test
        public void zuber_does_not_stop_reading()
        {
            String[] transmission = {".", "zuber", "-", "stop"};
            var zuberTransmitter = new MockTransmitter(transmission);
            var zuberListener = new Agent(zuberTransmitter);
            var scanner = new Scanner(zuberListener);
            assertEquals("a", scanner.next());
        }

        @Test
        public void zuber_is_transmitted_after_finishing_word()
        {
            String[] transmission = {".", "zuber", "-", "stop"};
            var zuberTransmitter = new MockTransmitter(transmission);

            var zuberListener = new Agent(zuberTransmitter, "foo/bar/baz/");
            new Scanner(zuberListener).next(); // Consume Zuber

            // scanner that outputs zuberListener transmission
            var scanner = new Scanner(new Agent(zuberListener));
            scanner.useDelimiter("/");


            assertEquals("foo", scanner.next());
            assertEquals("zuber", scanner.next());
            assertEquals("bar", scanner.next());
        }

        @Test
        public void zuber_is_transmitted_each_time_it_occurs()
        {
            // transmitted message: a/a
            String[] transmission = {".", "zuber", "-", "stop", "-", ".", ".", "-", ".", "stop", "zuber", ".", "-", "stop"};
            var zuberTransmitter = new MockTransmitter(transmission);

            var zuberListener = new Agent(zuberTransmitter, "foo/bar/baz/");
            var zuberTrigger = new Scanner(zuberListener);
            zuberTrigger.useDelimiter("/");
            zuberTrigger.next();

            // scanner that outputs zuberListener transmission
            var scanner = new Scanner(new Agent(zuberListener));
            scanner.useDelimiter("/");


            assertEquals("foo", scanner.next());
            assertEquals("zuber", scanner.next());
            assertEquals("bar", scanner.next());
            zuberTrigger.next();
            // transmission of baz hasn't started yet, so zuber is transmitted immediately
            assertEquals("zuber", scanner.next());
            assertEquals("baz", scanner.next());
        }
    }
}

// Below are two utility classes to simplify testing

/**
 * Emulates reading-only agent
 * Receives and stores data transmitted by agents
 */
class MockReceiver
{
    String[] expected;
    ArrayList<String> actual;

    public MockReceiver() { this(null); }

    public MockReceiver(String[] expected)
    {
        this.expected = expected;
        actual = new ArrayList<>();
    }

    public String receiveOne(TajnyAgent from)
    {
        try
        {
            from.transmituj();
            return "-";
        }
        catch (Exception e)
        {
            return switch (e.getClass().getSimpleName())
                    {
                        case "Zuber" -> "zuber";
                        case "RozmowaKontrolowana" -> "kontrola";
                        case "Stop" -> "stop";
                        case "Koniec" -> "koniec";
                        default -> ".";
                    };
        }
    }

    public void receiveAll(TajnyAgent from)
    {
        while (true)
        {
            var received = receiveOne(from);
            if (received.equals("koniec"))
                break;
            else
                actual.add(received);
        }
    }

    public ArrayList<String> receiveN(TajnyAgent from, int n)
    {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < n; i++)
            result.add(receiveOne(from));
        return result;
    }
}

/**
 * Emulates transmitting-only agent
 * Converts stream of strings into stream of exceptions
 */
class MockTransmitter implements TajnyAgent
{
    int index = 0;
    boolean finished = false;
    String[] codepoints;

    public MockTransmitter(String[] codepoints)
    {
        this.codepoints = codepoints;
    }

    @Override
    public void transmituj()
    {
        if (finished)
            fail("Attempt to read after transmission has finished.");

        if (index == codepoints.length)
        {
            finished = true;
            throw new Koniec();
        }
        else
        {
            var data = codepoints[index];
            index++;
            switch (data)
            {
                case "." -> throw new RuntimeException();
                case "stop" -> throw new Stop();
                case "kontrola" -> throw new RozmowaKontrolowana();
                case "zuber" -> throw new Zuber();
                // koniec is handled automatically
            }
        }
    }
}
