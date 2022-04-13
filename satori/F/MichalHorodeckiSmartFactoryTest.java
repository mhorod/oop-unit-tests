import org.junit.*;

import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.lang.reflect.Executable;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class MichalHorodeckiSmartFactoryTest
{
    // Test invoking methods already implemented on given object
    // Tested methods contain only exact types
    public static class FirstPhaseSimpleTest
    {

        interface MockInterface
        {
            void interface_returns_void();
            String no_arguments();
            String has_arguments(String a);
            String only_match_with_that_name(String a);
            String package_private(String a);
            void has_side_effects();
            String throws_an_exception() throws Exception;
            String throws_explicit_runtime_exception();
            String throws_wrong_exception() throws Exception;
        }

        class MockClass
        {
            public static class MockException extends Exception { }

            int x = 0;
            boolean interface_returns_void_was_called = false;

            public String interface_returns_void()
            {
                interface_returns_void_was_called = true;
                return "abc";
            }

            public String no_arguments()
            {
                return "foo";
            }

            public String has_arguments(String a)
            {
                return "bar";
            }

            public String only_match_with_that_name(String a)
            {
                return "baz";
            }

            public String only_match_with_that_name(Integer a)
            {
                return "boo";
            }

            String package_private(String a)
            {
                return "bam";
            }

            void has_side_effects()
            {
                x = 42;
            }

            String throws_an_exception() throws Exception
            {
                throw new MockException();
            }

            String throws_explicit_runtime_exception() throws RuntimeException
            {
                return "quz";
            }

            String throws_wrong_exception() throws Throwable
            {
                return "quux";
            }
        }

        @Test
        public void non_void_object_method_can_be_used_if_interface_returns_void()
        {
            var obj = new MockClass();
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            proxy.interface_returns_void();
            assertTrue(obj.interface_returns_void_was_called);
        }

        @Test
        public void methods_with_no_arguments_are_proxied()
        {
            var obj = new MockClass();
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            assertEquals(obj.no_arguments(), proxy.no_arguments());
        }

        @Test
        public void methods_with_arguments_are_proxied()
        {
            var obj = new MockClass();
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            assertEquals(obj.has_arguments(""), proxy.has_arguments(""));
        }

        @Test
        public void methods_with_repeating_name_are_proxied_with_correct_arguments()
        {
            var obj = new MockClass();
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            assertEquals(obj.only_match_with_that_name(""),
                         proxy.only_match_with_that_name(""));
        }

        @Test
        public void package_private_methods_are_proxied()
        {
            var obj = new MockClass();
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            assertEquals(obj.package_private(""), proxy.package_private(""));
        }

        @Test
        public void proxy_causes_side_effects_on_supplied_object()
        {
            var obj = new MockClass();
            assertEquals(0, obj.x);
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            proxy.has_side_effects();
            assertEquals(42, obj.x);
        }

        @Test
        public void exceptions_thrown_from_proxied_class_are_unwrapped()
        {
            var obj = new MockClass();
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            try
            {
                proxy.throws_an_exception();
                fail("obj.throws_an_exception should be called");
            }
            catch (Exception e)
            {
                assertEquals(MockClass.MockException.class, e.getClass());
            }
        }

        @Test
        public void methods_that_explicitly_throw_runtime_exception_are_proxied()
        {
            var obj = new MockClass();
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            assertEquals(obj.throws_explicit_runtime_exception(),
                         proxy.throws_explicit_runtime_exception());
        }

        @Test
        public void methods_that_throw_wrong_exceptions_are_not_proxied()
        {
            var obj = new MockClass();
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            String expected = "", actual;
            try
            {
                actual = proxy.throws_wrong_exception();
            }
            catch (Throwable e)
            {
                fail("No exception is actually thrown");
                return;
            }
            assertEquals(expected, actual);
        }
    }

    // Test invoking methods already implemented on given object
    // Tested methods contain different types than the interface
    // i.e. return types, parameters, and exceptions inherit from appropriate types
    public static class FirstPhasePolymorphismTest
    {
        static class ExceptionA extends Exception { }

        static class ExceptionB extends Exception { }

        static class ExceptionC extends ExceptionA { }

        static class ExceptionD extends ExceptionB { }

        interface MockInterface
        {
            Number simple_return_specification();

            String simple_argument_specification(String o);
            String multiple_matches_argument_specification(Integer o);

            String multiarg_argument_specification(Integer a, String b);

            String simple_exception_specification() throws Throwable;
            String multiexception_exception_specification()
                    throws ExceptionA, ExceptionB;

        }

        static class MockClass
        {
            Integer simple_return_specification() { return 42; }

            String simple_argument_specification(String o)
            {
                return "foo";
            }

            String multiple_matches_argument_specification(Integer o)
            {
                return "bar";
            }

            String multiple_matches_argument_specification(Object o)
            {
                return "baz";
            }

            String multiarg_argument_specification(Object a, Object b)
            {
                return "qux";
            }

            String simple_exception_specification() throws Exception
            {
                return "quz";
            }

            String multiexception_exception_specification()
                    throws ExceptionC, ExceptionD
            {
                return "spam";
            }
        }

        @Test
        public void simple_return_specification()
        {
            MockClass obj = new MockClass();
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            assertEquals(obj.simple_return_specification(),
                         proxy.simple_return_specification());
        }


        @Test
        public void simple_argument_specification()
        {
            MockClass obj = new MockClass();
            String a = "";
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            assertEquals(obj.simple_argument_specification(a),
                         proxy.simple_argument_specification(a));
        }

        @Test
        public void multiple_matches_argument_specification()
        {

            MockClass obj = new MockClass();
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            try
            {
                proxy.multiple_matches_argument_specification(42);
                fail("Expected HellNoException");
            }
            catch (SmartFactory.HellNoException e)
            {

            }
        }

        @Test
        public void multiarg_argument_specification()
        {
            MockClass obj = new MockClass();
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            assertEquals(obj.multiarg_argument_specification(42, ""),
                         proxy.multiarg_argument_specification(42, ""));
        }

        @Test
        public void simple_exception_specification()
        {
            MockClass obj = new MockClass();
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            try
            {
                assertEquals(obj.simple_exception_specification(),
                             proxy.simple_exception_specification());
            }
            catch (Throwable e)
            {
                fail("No exception is actually thrown");
            }

        }

        @Test
        public void multiexception_exception_specification()
        {
            MockClass obj = new MockClass();
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, obj);
            try
            {
                assertEquals(obj.multiexception_exception_specification(),
                             proxy.multiexception_exception_specification());
            }
            catch (Throwable e)
            {
                fail("No exception is actually thrown");
            }

        }
    }

    // Test invoking methods that are not implemented on given object
    // and constructing the return type
    public static class SecondPhaseTest
    {
        interface MockInterface
        {
            void returns_void();
            int returns_int();

            ContainsArray contains_array();
            InitializedFields initialized_fields();
            CycleA cycle();
            InheritingFields inheriting_fields();
            InheritingFieldsWithPrivateConstructors inheriting_fields_with_private_constructors();
            StaticFields static_fields();
            ThrowingConstructor throwing_constructor();
            MemberWithPrivateConstructor member_with_private_constructor();
            StaticFieldWithPrivateConstructor static_field_with_private_constructor();
        }

        static class ContainsArray
        {
            public Object[] a;
            public Number[] b;
            public Integer[] c;
        }

        static class InitializedFields
        {
            public static String initialized_string = "KOT";
            public static String uninitialized_string;

            public Integer[] initialized_array = {1, 2, 3};
            public Integer[] uninitialized_array;
        }

        static class CycleA
        {
            CycleB next;
        }

        static class CycleB
        {
            CycleA next;
        }

        static class InheritingFields
        {
            static class Base { }

            static class Derived extends Base { }

            static class DerivedDerived extends Derived { }


            // Initializing in this order will create three different objects
            Base base;
            Derived derived;
            DerivedDerived derivedDerived;
        }


        // Similar example as above, but all classes have private constructors
        // which forces taking already initialized bottom class
        static class InheritingFieldsWithPrivateConstructors
        {
            static class Base
            {
                private Base() { }
            }

            static class Derived extends Base
            {
                private Derived() { }
            }

            static class DerivedDerived extends Derived
            {
                private DerivedDerived() { }
            }

            // Initializing in this order will throw an exception
            Base base;
            Derived derived;
            DerivedDerived derivedDerived = new DerivedDerived();

        }

        static class StaticFields
        {
            static Object uninitialized;
            static Object initialized = new Object();
        }

        static class ThrowingConstructor
        {
            public ThrowingConstructor()
            {
                throw new RuntimeException("hahaha");
            }
        }

        static class MemberWithPrivateConstructor
        {
            static class PrivateConstructor
            {
                private PrivateConstructor() { }
            }

            static class PublicConstructor
            {
                PrivateConstructor obj = new PrivateConstructor();
            }

            PrivateConstructor privateConstructor;
            PublicConstructor publicConstructor;
        }

        static class StaticFieldWithPrivateConstructor
        {
            static class PrivateConstructor
            {
                private PrivateConstructor() { }
            }

            static PrivateConstructor pc;
        }


        @Test
        public void void_is_not_constructed()
        {
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, null);
            try
            {
                proxy.returns_void();
            }
            catch (Exception e)
            {
                fail("No exception should be thrown");
            }
        }

        @Test
        public void class_containing_array_field()
        {
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, null);
            ContainsArray x = proxy.contains_array();
            assertNull(x.a);
            assertNull(x.b);
            assertNull(x.c);
        }

        @Test
        public void initialized_fields_cannot_be_used_to_initialize_other_fields()
        {
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, null);
            InitializedFields x = proxy.initialized_fields();
            assertNotEquals(x.initialized_array, x.uninitialized_array);
            assertNotEquals(x.initialized_string, x.uninitialized_string);
        }

        @Test
        public void cycle()
        {
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, null);
            CycleA a = proxy.cycle();
            assertEquals(a, a.next.next);
            assertEquals(a.next, a.next.next.next);
        }

        @Test
        public void inheriting_fields_reference_the_same_object()
        {
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, null);
            InheritingFields x = proxy.inheriting_fields();
            assertEquals(x.base, x.derived);
            assertEquals(x.derived, x.derivedDerived);
        }

        @Test
        public void static_fields_are_initialized()
        {
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, null);
            StaticFields x = proxy.static_fields();
            assertNotNull(StaticFields.uninitialized);
        }

        @Test
        public void static_fields_cannot_be_used_to_initialize_other_field()
        {
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, null);
            StaticFields x = proxy.static_fields();
            assertNotSame(StaticFields.uninitialized, StaticFields.initialized);
        }


        @Test
        public void constructing_class_with_throwing_constructor_fails()
        {
            MockInterface proxy = SmartFactory.fixIt(MockInterface.class, null);
            assertThrows(SmartFactory.HellNoException.class,
                         proxy::throwing_constructor);
        }
    }
}
