import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListIsFunctionTest {
    @Test
    public void testBasic() {
        ListIsFunction temp = new ListIsFunction();
        assertEquals(Object.class, temp.getClass().getSuperclass());
        List tempList = temp.asList();
        Map tempMap = temp.asMap();
        assertEquals("[]", tempList.toString());
        assertEquals("{}",tempMap.toString());
        tempList.add("Ala");
        tempList.add("ma");
        tempList.add("kota");

        assertEquals("{0=Ala, 1=ma, 2=kota}",tempMap.toString());
        assertEquals("[0, 1, 2]",tempMap.keySet().toString());
        assertEquals("[Ala, ma, kota]",tempMap.values().toString());
        tempMap.put(1, "miala");
        assertEquals("[Ala, miala, kota]",tempList.toString());
        tempMap.put(3,"malego");
        assertEquals("[Ala, miala, kota, malego]",tempList.toString());
        tempMap.remove(3);
        assertEquals("[Ala, miala, kota]",tempList.toString());

        Iterator it = tempMap.keySet().iterator();
        assertEquals("Ala",tempMap.get(it.next()));
        assertEquals("miala",tempMap.get(it.next()));
        try {
            it.remove();
            fail("No exception!");
        } catch(Exception e) {
            assertEquals(java.lang.IllegalStateException.class, e.getClass());
        }
        assertEquals("kota",tempMap.get(it.next()));
        it.remove();
        assertEquals("[Ala, miala]", tempList.toString());

        try {
            tempMap.remove(0);
            fail("No exception!");
        } catch(Exception e) {
            assertEquals(java.lang.IllegalArgumentException.class, e.getClass());
        }
        tempMap.remove(1);
        tempMap.remove(0);
        assertEquals("[]", tempList.toString());
        assertTrue(tempList.isEmpty());
        assertTrue(tempMap.isEmpty());
    }
}