import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DominikMatuszekPhoneBookTest {

    @Test
    public void adding_elements_with_hostile_intent(){
        PhoneBook a = new PhoneBook();
        a.add("asdad");
        a.add("3 2 1");
        a.add("helo helo 3 2 0");
        a.add("slyszales kiedys zart o falowanych blaszkach?");
        a.add("01111111");
        a.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        a.add("111-111-111");
        a.add("111-111-11");
        a.add("111-1111-11");
        a.add("przychodzi matematyk do baru");
        a.add("atam0000000");
        a.add("zbiorpusty0");
        a.add("zbi-orp-ust");
        a.add("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        assertEquals("{\n  111-111-111\n}\n",a.toString());
    }



}
