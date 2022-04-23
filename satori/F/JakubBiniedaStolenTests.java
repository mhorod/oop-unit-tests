import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;

import org.junit.*;

public class JakubBiniedaStolenTests {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void Test0() {
        String ala = "Ala has a cat";
		//Warning, raw type
		Collection col = SmartFactory.fixIt(Collection.class, ala);
		System.out.println(col);
		try {
			col.iterator();
		}catch(SmartFactory.HellNoException e){
			System.out.println("Well.... doesn't work.");
		}


        assertEquals("Ala has a cat\nWell.... doesn't work.", outContent.toString().trim());
    }

    public static class A { }
    public static class B extends A {
        public B(int i) { }
    }

    public static interface OutA {
        A test();
    }

    public static class OutAClass implements OutA {
        public A test() {
            System.out.println("Test2inA");
            return new A();
        }
    }

    public static interface OutB {
        B test();
    }

    @Test 
    public void Test3() {
        OutB fakeOutB = SmartFactory.fixIt(OutB.class, new OutAClass());
        try {
            System.out.println(fakeOutB.test());
        } catch (SmartFactory.HellNoException e) {   System.out.println("unhandled - OK");  }
        assertEquals("unhandled - OK", outContent.toString().trim());
    }

    class Bad {}

    public static interface ThrowingA {
        Bad test() throws Exception;
    }
    public static class NoExcClass {
        public Bad test(){ System.out.println("NoExcClass");  return null; }
    }

    @Test
    public void Test4() {
        ThrowingA tA = SmartFactory.fixIt(ThrowingA.class, new NoExcClass());
        try {
            tA.test();
        } catch(Exception oof) {
            System.err.println(oof);
            System.out.println("not good");
        }

        assertEquals("NoExcClass", outContent.toString().trim());
    }

    public static class Parent {}
    public static class Child extends Parent {}

    public static class Wrapper {
        public Child child;
    }

    public static class HasBoth {
        public Parent parent;
        public Wrapper wrapper;
    }

    interface Creator {
        HasBoth create();
    }

    @Test
    public void ThanksJakubOskwarek() {
        var proxy = SmartFactory.fixIt(Creator.class, null);
        var fabricated = proxy.create();
        assertSame(fabricated.wrapper.child, fabricated.parent);
    }

    public static class C {
        public static Object[] ar;
    }
    public interface GetC {    C test();    }

    @Test
    public void arrayGood() {     
        try {
            C c = SmartFactory.fixIt(GetC.class, null).test(); 
        } catch(Exception ohNo) {
            fail("Array is not a exception");
        }
   }

    static class D {
        public int i;
        D(int i) {this.i = i;}
    }

    static class E extends D {
        E() {super(0);}
    }

    interface F {
        D getD();
        E getE();
    }

    static class giveB {
        public E getE() {
            return new E();
        }
    }

    @Test(expected=SmartFactory.HellNoException.class)
    public void iunnoAnymore(){
        F f = SmartFactory.fixIt(F.class, new giveB());
        D d = f.getD();
    }
}
