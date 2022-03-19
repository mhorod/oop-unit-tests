import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
@RunWith(Enclosed.class)
public class MichalHorodeckiListIsFunctionTest
{
    // Test operations that should not throw an exception
    public static class OperationsWithoutExceptionsTest
    {
        @Test
        public void new_list_is_empty()
        {
            var list = new ListIsFunction();
            assertTrue(list.asList().isEmpty());
            assertTrue(list.asMap().isEmpty());
            assertTrue(list.asMap().entrySet().isEmpty());
            assertTrue(list.asMap().keySet().isEmpty());
        }

        @Test
        public void new_list_has_size_of_zero()
        {
            var list = new ListIsFunction();
            assertEquals(0, list.asList().size());
            assertEquals(0, list.asMap().size());
            assertEquals(0, list.asMap().entrySet().size());
            assertEquals(0, list.asMap().keySet().size());
        }

        @Test
        public void adding_element_to_list_adds_it_to_map()
        {
            var list = new ListIsFunction();
            list.asList().add("A");
            assertFalse(list.asMap().isEmpty());
            assertEquals(1, list.asMap().size());
            assertTrue(list.asMap().containsKey(0));
            assertTrue(list.asMap().containsValue("A"));
        }

        @Test
        public void adding_element_to_map_adds_it_to_list()
        {
            var list = new ListIsFunction();
            list.asMap().put(0, "A");
            list.asMap().put(1, "B");
            assertFalse(list.asList().isEmpty());
            assertEquals(2, list.asList().size());
            assertTrue(list.asList().contains("A"));
            assertTrue(list.asList().contains("B"));
        }

        @Test
        public void removing_element_from_map_removes_it_from_list()
        {
            var list = new ListIsFunction();
            list.asList().add("A");
            list.asList().add("B");
            list.asMap().remove(1);
            assertEquals("[A]", list.asList().toString());
        }

        @Test
        public void list_allows_removing_every_index()
        {
            var list = new ListIsFunction();
            list.asList().add("A");
            list.asList().add("B");
            list.asList().add("C");
            list.asList().remove(1);
            list.asList().remove(0);
            list.asList().remove(0);
            assertTrue(list.asList().isEmpty());
        }

        @Test
        public void clearing_list_clears_map()
        {
            var list = new ListIsFunction();
            list.asList().add("A");
            list.asList().add("B");
            list.asList().clear();

            assertTrue(list.asMap().isEmpty());
            assertEquals(0, list.asMap().size());
        }

        @Test
        public void map_prints_elements_in_order_of_list()
        {
            var list = new ListIsFunction();
            for (int i = 5; i > 0; i--)
                list.asList().add(i);

            assertEquals("{0=5, 1=4, 2=3, 3=2, 4=1}", list.asMap().toString());
        }

        @Test
        public void map_iterates_over_elements_in_order_of_list()
        {
            var list = new ListIsFunction();
            for (int i = 0; i < 5; i++)
                list.asList().add(i);

            var iterator = list.asMap().keySet().iterator();
            for (int i = 0; i < 5; i++)
                assertEquals(i, iterator.next());
        }

        @Test
        public void list_accepts_objects_as_values()
        {
            var list = new ListIsFunction();
            list.asList().add(null);
            list.asList().add(new Object());
            list.asList().add(list);
            list.asList().add(1);
            list.asList().add("");
            assertEquals(5, list.asList().size());
            assertEquals(5, list.asMap().size());
        }

        @Test
        public void map_accepts_objects_as_values()
        {
            var list = new ListIsFunction();
            list.asMap().put(0, null);
            list.asMap().put(1, new Object());
            list.asMap().put(2, list);
            list.asMap().put(3, 4);
            list.asMap().put(4, "");
            assertEquals(5, list.asList().size());
            assertEquals(5, list.asMap().size());
        }

        @Test
        public void removing_key_out_of_list_bounds_does_nothing()
        {
            var list = new ListIsFunction();
            list.asList().add("A");
            list.asList().add("B");
            list.asMap().remove(-1);
            list.asMap().remove(2);
            assertEquals("[A, B]", list.asList().toString());
        }
    }

    // Tests if exceptions are thrown in appropriate places when using Map functionality
    public static class MapExceptionsTest
    {
        /**
         * Call a function with expectation of throwing an exception without a message
         */
        public static void assertThrowsWithoutMessage(Class exceptionClass, Runnable f)
        {
            try
            {
                f.run();
                fail("expected " + exceptionClass.getSimpleName() +
                     " but nothing was thrown");
            }
            catch (Exception e)
            {
                assertEquals(exceptionClass, e.getClass());
                assertNull(e.getMessage());
            }
        }

        @Test
        public void removing_key_from_beginning_throws_exception()
        {
            var list = new ListIsFunction();
            list.asList().add("A");
            list.asList().add("B");
            Runnable action = () -> list.asMap().remove(0);
            assertThrowsWithoutMessage(IllegalArgumentException.class, action);
        }

        @Test
        public void removing_key_from_middle_throws_exception()
        {
            var list = new ListIsFunction();
            list.asList().add("A");
            list.asList().add("B");
            list.asList().add("C");
            Runnable action = () -> list.asMap().remove(1);
            assertThrowsWithoutMessage(IllegalArgumentException.class, action);
        }

        @Test
        public void removing_iterator_without_calling_next_first_throws_exception()
        {
            var list = new ListIsFunction();
            list.asList().add("A");
            var iterator = list.asMap().keySet().iterator();
            Runnable action = iterator::remove;
            assertThrowsWithoutMessage(IllegalStateException.class, action);
        }

        @Test
        public void removing_iterator_from_beginning_throws_exception()
        {
            var list = new ListIsFunction();
            list.asList().add("A");
            list.asList().add("B");
            var iterator = list.asMap().keySet().iterator();
            iterator.next();
            Runnable action = iterator::remove;
            assertThrowsWithoutMessage(IllegalStateException.class, action);
        }

        @Test
        public void removing_iterator_from_middle_throws_exception()
        {
            var list = new ListIsFunction();
            list.asList().add("A");
            list.asList().add("B");
            list.asList().add("C");
            var iterator = list.asMap().keySet().iterator();
            iterator.next();
            iterator.next();
            Runnable action = iterator::remove;
            assertThrowsWithoutMessage(IllegalStateException.class, action);
        }

        @Test
        public void calling_next_on_iterator_after_last_element_throws_exception()
        {
            var list = new ListIsFunction();
            list.asList().add("A");
            list.asList().add("B");
            var iterator = list.asMap().keySet().iterator();
            iterator.next();
            iterator.next();
            Runnable action = iterator::next;
            assertThrowsWithoutMessage(NoSuchElementException.class, action);
        }

        @Test
        public void putting_null_key_throws_exception_without_message()
        {
            var list = new ListIsFunction();
            Runnable action = () -> list.asMap().put(null, "");
            assertThrowsWithoutMessage(NullPointerException.class, action);
        }

        @Test
        public void putting_non_integer_key_throws_exception_without_message()
        {
            var list = new ListIsFunction();
            Runnable action = () -> list.asMap().put("", "");
            assertThrowsWithoutMessage(ClassCastException.class, action);
        }

        @Test
        public void putting_key_out_of_bounds_throws_exception_without_message()
        {
            var list = new ListIsFunction();
            list.asList().add("A");
            Runnable add_before = () -> list.asMap().put(-1, "");
            Runnable add_after = () -> list.asMap().put(2, "");
            assertThrowsWithoutMessage(IllegalArgumentException.class, add_before);
            assertThrowsWithoutMessage(IllegalArgumentException.class, add_after);
        }

        @Test
        public void removing_null_key_throws_exception_without_message()
        {
            var list = new ListIsFunction();
            Runnable action = () -> list.asMap().remove(null);
            assertThrowsWithoutMessage(NullPointerException.class, action);
        }

        @Test
        public void removing_non_integer_key_throws_exception_without_message()
        {
            var list = new ListIsFunction();
            Runnable action = () -> list.asMap().remove("");
            assertThrowsWithoutMessage(ClassCastException.class, action);
        }


    }
}
