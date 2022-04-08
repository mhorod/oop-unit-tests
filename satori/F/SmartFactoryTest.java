import org.junit.*;

import static org.junit.Assert.*;

import java.util.*;


public class SmartFactoryTest
{
    @Test
    public void test0()
    {
        String ala = "Ala has a cat";
        //Warning, raw type
        Collection col = SmartFactory.fixIt(Collection.class, ala);
        System.out.println(col);
        try
        {
            col.iterator();
        }
        catch (SmartFactory.HellNoException e)
        {
            System.out.println("Well.... doesn't work.");
        }
    }

    public static class A { }

    public static class B
    {
        public String ala;
        public CharSequence ma;
    }

    public static class C
    {
        public static int i;
        public final int id = i++;
        public C other;
        public C yetAnother;
        public Object anObject;
    }

    public interface I
    {
        A test();
    }

    public interface J
    {
        B test();
    }

    public interface K
    {
        C test();
    }

    @Test
    public void testBasic()
    {
        assertNotNull(SmartFactory.fixIt(I.class, null).test());
        B b = SmartFactory.fixIt(J.class, null).test();
        assertEquals("", b.ala);
        assertEquals("", b.ma);
        assertTrue(b.ala == b.ma);
    }

    @Test
    public void testNumber()
    {
        C c = SmartFactory.fixIt(K.class, null).test();
        assertEquals(1, C.i);
        assertTrue(c == c.other);
        assertTrue(c == c.yetAnother);
        assertTrue(c == c.anObject);
        c = SmartFactory.fixIt(K.class, null).test();
        assertEquals(2, C.i);
        assertTrue(c == c.other);
        assertTrue(c == c.yetAnother);
        assertTrue(c == c.anObject);
    }


}
