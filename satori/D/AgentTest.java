
import org.junit.Test;
import java.util.Scanner;
import static org.junit.Assert.*;

public class AgentTest {
    @Test
    public void test1() {
        Scanner s = new Scanner(new Agent(new Agent("tajny/komunikat/państwa/podziemnego.")));
        assertEquals("tajny/komunikat/państwa/podziemnego.", s.next());
    }

    @Test
    public void test2() {
        String[] tekst = String.valueOf("pawiany wchodzą na ściany żyrafy wchodzą do szafy " +
                "pawiany wchodzą na ściany żyrafy wchodzą do szafy " +
                "pawiany wchodzą").split("\\s+");
        Scanner s = new Scanner(new Agent(new Agent()));
        s.useDelimiter("([/!])");
        for (String str : tekst)
            assertEquals(str, s.next());
    }
    @Test
    public void test3() {
        Agent agent = new Agent(new TajnyAgent() {
            int i;
            @Override
            public void transmituj() {
                if(i++ == 0) throw new Zuber();
                throw new Koniec();
            }
        });
        Scanner s = new Scanner(agent), t = new Scanner(new Agent(agent));
        t.useDelimiter("([/!])");
        assertEquals("pawiany",t.next());
        assertFalse(s.hasNext());
        assertEquals("zuber", t.next());
        assertEquals("wchodzą", t.next());

    }
}
