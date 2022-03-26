import org.junit.*;
import static org.junit.Assert.*;
import java.util.Scanner;

@SuppressWarnings({"rawtypes", "unchecked"})
public class KacperTopolskiAgentTest {
    static class SimpleAgent implements TajnyAgent {
        int i = 0;
        RuntimeException[] var;
        SimpleAgent(RuntimeException... varargs) {
            var = varargs;
        }
        @Override
        public void transmituj() {
            RuntimeException e = i < var.length ? var[i++] : new Koniec();
            if (e != null)
                throw e;
        }
    };

    void does_ch_work(String ch, RuntimeException... varargs) {
        Agent A = new Agent(ch);

        for (int i = 0; i < varargs.length; ++i) {
            if (varargs[i] == null) {
                A.transmituj();
                continue;
            }

            try {
                A.transmituj();
                fail("no exception");
            } catch (Exception e) {
                var cl = e.getClass();
                var ex_cl = varargs[i].getClass();

                if (ex_cl == RuntimeException.class)
                    assertFalse(cl == Koniec.class || cl == RozmowaKontrolowana.class || cl == Stop.class || cl == Zuber.class);
                else
                    assertEquals(ex_cl, cl);
            }
        }

        SimpleAgent S = new SimpleAgent(varargs);
        Agent B = new Agent(S);
        Scanner SB = new Scanner(B);
        assertEquals(ch, SB.next());
    }

    @Test
    public void alphabet_test() {
        does_ch_work("a", new RuntimeException(), null, new Stop(), new Koniec());
        does_ch_work("ą", new RuntimeException(), null, new RuntimeException(), null, new Stop(), new Koniec());
        does_ch_work("b", null, new RuntimeException(), new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("c", null, new RuntimeException(), null, new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("ć", null, new RuntimeException(), null, new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("d", null, new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("e", new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("ę", new RuntimeException(), new RuntimeException(), null, new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("f", new RuntimeException(), new RuntimeException(), null, new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("g", null, null, new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("h", new RuntimeException(), new RuntimeException(), new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("i", new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("j", new RuntimeException(), null, null, null, new Stop(), new Koniec());
        does_ch_work("k", null, new RuntimeException(), null, new Stop(), new Koniec());
        does_ch_work("l", new RuntimeException(), null, new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("ł", new RuntimeException(), null, new RuntimeException(), new RuntimeException(), null, new Stop(), new Koniec());
        does_ch_work("m", null, null, new Stop(), new Koniec());
        does_ch_work("n", null, new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("ń", null, null, new RuntimeException(), null, null, new Stop(), new Koniec());
        does_ch_work("o", null, null, null, new Stop(), new Koniec());
        does_ch_work("ó", null, null, null, new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("p", new RuntimeException(), null, null, new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("q", null, null, new RuntimeException(), null, new Stop(), new Koniec());
        does_ch_work("r", new RuntimeException(), null, new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("s", new RuntimeException(), new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("ś", new RuntimeException(), new RuntimeException(), new RuntimeException(), null, new RuntimeException(), new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("t", null, new Stop(), new Koniec());
        does_ch_work("u", new RuntimeException(), new RuntimeException(), null, new Stop(), new Koniec());
        does_ch_work("v", new RuntimeException(), new RuntimeException(), new RuntimeException(), null, new Stop(), new Koniec());
        does_ch_work("w", new RuntimeException(), null, null, new Stop(), new Koniec());
        does_ch_work("x", null, new RuntimeException(), new RuntimeException(), null, new Stop(), new Koniec());
        does_ch_work("y", null, new RuntimeException(), null, null, new Stop(), new Koniec());
        does_ch_work("z", null, null, new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("ź", null, null, new RuntimeException(), new RuntimeException(), null, new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("ż", null, null, new RuntimeException(), new RuntimeException(), null, new Stop(), new Koniec());
        does_ch_work("0", null, null, null, null, null, new Stop(), new Koniec());
        does_ch_work("1", new RuntimeException(), null, null, null, null, new Stop(), new Koniec());
        does_ch_work("2", new RuntimeException(), new RuntimeException(), null, null, null, new Stop(), new Koniec());
        does_ch_work("3", new RuntimeException(), new RuntimeException(), new RuntimeException(), null, null, new Stop(), new Koniec());
        does_ch_work("4", new RuntimeException(), new RuntimeException(), new RuntimeException(), new RuntimeException(), null, new Stop(), new Koniec());
        does_ch_work("5", new RuntimeException(), new RuntimeException(), new RuntimeException(), new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("6", null, new RuntimeException(), new RuntimeException(), new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("7", null, null, new RuntimeException(), new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("8", null, null, null, new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("9", null, null, null, null, new RuntimeException(), new Stop(), new Koniec());
        does_ch_work(".", new RuntimeException(), null, new RuntimeException(), null, new RuntimeException(), null, new Stop(), new Koniec());
        does_ch_work(",", null, null, new RuntimeException(), new RuntimeException(), null, null, new Stop(), new Koniec());
        does_ch_work("!", null, new RuntimeException(), null, new RuntimeException(), null, null, new Stop(), new Koniec());
        does_ch_work("?", new RuntimeException(), new RuntimeException(), null, null, new RuntimeException(), new RuntimeException(), new Stop(), new Koniec());
        does_ch_work("/", null, new RuntimeException(), new RuntimeException(), null, new RuntimeException(), new Stop(), new Koniec());
    }
}
