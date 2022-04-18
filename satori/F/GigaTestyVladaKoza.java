import org.junit.Test;

import static org.junit.Assert.*;

public class GigaTestyVladaKoza {
    interface Interface {
        Object foo(String c) throws Exception;
    }

    class Class {
        String foo(Object c) throws IllegalAccessException {
            return c.toString();
        }
    }

    @Test
    public void returnTypeArgumentsAndErrors_can_be_casted() {
        Interface proxy = SmartFactory.fixIt(Interface.class, new Class());
        try {
            assertEquals(proxy.foo("42"), "42");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface Interface2 {
        void foo(Integer a);
    }

    class Class2 {
        void foo(Integer c) {

        }

        void foo(Number c) {

        }
    }

    @Test
    public void too_many_good_functions_frows_hello() throws Exception {
        Interface2 proxy = SmartFactory.fixIt(Interface2.class, new Class2());
        try {
            proxy.foo(4);
            fail();
        } catch (SmartFactory.HellNoException e) {

        }
    }

    public static class ClassCieslik {
        public static Object[] ar;
    }

    public interface InterfaceCieslik {
        ClassCieslik test();
    }

    @Test
    public void testCieslik() {
        ClassCieslik a = SmartFactory.fixIt(InterfaceCieslik.class, null).test();
    }

    public interface Class2Cieslik {
        Interface2Cieslik test();
    }

    public static class Interface2Cieslik {
        public static Interface2Cieslik a;
        public static String b;
        public static String c = "KOT";

    }

    @Test
    public void test2Cieslik() {
        Interface2Cieslik a = SmartFactory.fixIt(Class2Cieslik.class, null).test();
        assertNotNull(a.a);
        assertEquals("", a.b);
        assertEquals("KOT", a.c);
    }

    public interface InterfaceMikos {
        String throwsE() throws Exception;

        String doesntThrowE();
    }

    public static class ClassMikos {
        String throwsE() {
            return "good";
        }

        String doesntThrowE() throws Exception {
            return "bad";
        }
    }

    @Test
    public void test1Mikos() {
        assertNotEquals("bad", SmartFactory.fixIt(InterfaceMikos.class, new ClassMikos())
                .doesntThrowE());
        try {
            assertEquals(SmartFactory.fixIt(InterfaceMikos.class, new ClassMikos()).throwsE(), "good");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
