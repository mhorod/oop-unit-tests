import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SzymonWojtulewiczTests {

    static class ClassWithAPrivateConstructor {
        private ClassWithAPrivateConstructor(){};
    }

    interface SimpleInterface{
        ClassWithAPrivateConstructor foo();
    }

    @Test
    public void onlyUsePublicConstructors(){
        SimpleInterface dummy = SmartFactory.fixIt(SimpleInterface.class, null);
        assertThrows(SmartFactory.HellNoException.class, dummy::foo);
    }

    interface SimpleExceptionThrower{
        String foo() throws Exception;
        String goo(Integer a) throws Exception;
    }

    static class ComplexExceptionThrower{
        String foo() throws IOException {
            return "complex";
        }
        String goo(Integer a) throws Throwable {
            // Exception e = (Throwable) t; //won't work
            return "Exception.class is not assignable from Throwable.class :((";
        }
        String goo(Number a) throws IOException {
            return "But this should work";
        }
    }

    static class JustThrower {
        public static class MyOwnException extends Exception {}
        String foo() throws MyOwnException {
            throw new MyOwnException();
        }
    }

    @Test
    public void exceptionsMatchingScopes() throws Exception {
        SimpleExceptionThrower dummy = SmartFactory.fixIt(SimpleExceptionThrower.class, new ComplexExceptionThrower());
        assertEquals("complex", dummy.foo());
    }
    @Test
    public void exceptionsNotMatchingScopes() throws Exception {
        SimpleExceptionThrower dummy = SmartFactory.fixIt(SimpleExceptionThrower.class, new ComplexExceptionThrower());
        assertEquals("But this should work", dummy.goo(0));
    }

    @Test
    public void exceptionsUnwrapping() {
        SimpleExceptionThrower dummy = SmartFactory.fixIt(SimpleExceptionThrower.class, new JustThrower());
        assertThrows(JustThrower.MyOwnException.class, dummy::foo);
    }

    static class Parent{
        public First first;
        public Second second;
        public Third third;
    }

    static class First{
        public Second next;
    }

    static class Second{
        public Third next;
    }

    static class Third{
        public First next;
    }

    interface ParentGetter{
        Parent foo();
    }

    @Test
    public void cyclicReferences(){
        ParentGetter dummy = SmartFactory.fixIt(ParentGetter.class, null);
        Parent parent = dummy.foo();
        assertSame(parent.first, parent.third.next);
        assertSame(parent.second, parent.first.next);
        assertSame(parent.third, parent.second.next);
    }

    static class ClassWithAPrivateField{
        private String thisIsAPrivateField;
    }

    interface ClassWithAPrivateFieldGetter{
        ClassWithAPrivateField get();
    }

    @Test
    public void privateFieldsAreOfNoInterest(){
        ClassWithAPrivateFieldGetter dummy = SmartFactory.fixIt(ClassWithAPrivateFieldGetter.class, null);

        ClassWithAPrivateField secret = dummy.get();

        assertNull(secret.thisIsAPrivateField);
    }
}