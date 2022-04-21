import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.*;

public class KacperTopolskiSmartFactoryTrollTests {
    static class A {

    }
    static class B extends A {
        public B(int x) {

        }
    }
    static class C {
        public A a;
        public B b = new B(0);
    }
    interface X {
        C f();
    }
    @Ignore
    @Test
    public void troll1() {
        C c = SmartFactory.fixIt(X.class, null).f();
    }

    static class D1 {
        public E1 e;
        public F f;
        public D1() {

        }
        public D1(int x) {

        }
    }
    static class E1 extends D1 {
        public E1() {
            f = new F(0);
        }
    }
    static class D2 {
        public E2 e;
        public F f;
        public D2() {
            f = new F(0);
            e = new E2();
            e.f = f;
            e.e = e;
        }
        public D2(int x) {

        }
    }
    static class E2 extends D2 {
        public E2() {
            super(0);
        }
    }
    static class F {
        public boolean bool;
        public F() {
            bool = false;
        }
        public F(int x) {
            bool = true;
        }
    }
    interface Y {
        D1 d1();
        D2 d2();
    }

    @Ignore
    @Test
    public void troll2() {
        Y y = SmartFactory.fixIt(Y.class, null);

        D1 d1 = y.d1();
        assertEquals(E1.class, d1.getClass());
        assertSame(d1, d1.e);
        assertTrue(d1.f.bool);

        D2 d2 = y.d2();
        assertEquals(D2.class, d2.getClass());
        assertTrue(d2.f.bool);
    }
}
