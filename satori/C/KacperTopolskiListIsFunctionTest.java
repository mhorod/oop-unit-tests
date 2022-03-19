import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
@RunWith(Enclosed.class)
public class KacperTopolskiListIsFunctionTest {
    public static class SizeTest {
        @Test
        public void is_empty_works_correctly() {
            ListIsFunction f = new ListIsFunction();

            assertTrue(f.asList().isEmpty());
            assertTrue(f.asMap().isEmpty());
            assertTrue(f.asMap().entrySet().isEmpty());
            assertTrue(f.asMap().keySet().isEmpty());

            assertEquals(0, f.asList().size());
            assertEquals(0, f.asMap().size());
            assertEquals(0, f.asMap().entrySet().size());
            assertEquals(0, f.asMap().keySet().size());

            f.asList().add(50);

            assertFalse(f.asList().isEmpty());
            assertFalse(f.asMap().isEmpty());
            assertFalse(f.asMap().entrySet().isEmpty());
            assertFalse(f.asMap().keySet().isEmpty());

            assertEquals(1, f.asList().size());
            assertEquals(1, f.asMap().size());
            assertEquals(1, f.asMap().entrySet().size());
            assertEquals(1, f.asMap().keySet().size());

            f.asList().remove(0);

            assertTrue(f.asList().isEmpty());
            assertTrue(f.asMap().isEmpty());
            assertTrue(f.asMap().entrySet().isEmpty());
            assertTrue(f.asMap().keySet().isEmpty());

            assertEquals(0, f.asList().size());
            assertEquals(0, f.asMap().size());
            assertEquals(0, f.asMap().entrySet().size());
            assertEquals(0, f.asMap().keySet().size());
        }
        @Test
        public void size_works_correctly() {
            ListIsFunction f = new ListIsFunction();
            for (int i = 0; i < 50; ++i) {
                assertEquals(f.asList().size(), i);
                assertEquals(f.asMap().size(), i);
                assertEquals(f.asMap().entrySet().size(), i);
                assertEquals(f.asMap().keySet().size(), i);

                f.asList().add((i & 1) == 1 ? "oop" : 42);
            }

        }
        @Test
        public void size_is_unbounded() {
            ListIsFunction f = new ListIsFunction();
            for (int i = 0; i < 12345; ++i) {
                assertEquals(2*i, f.asList().size());
                assertEquals(2*i, f.asMap().size());

                f.asMap().put(2*i, i);
                f.asList().add(-1);
            }
        }
    }
    public static class MapRemoveTest {
        @Test
        public void map_remove_on_empty_does_nothing() {
            ListIsFunction f = new ListIsFunction();

            f.asMap().remove(-10);
            assertTrue(f.asList().isEmpty());

            f.asMap().remove(-1);
            assertTrue(f.asMap().isEmpty());

            f.asMap().remove(0);
            assertTrue(f.asList().isEmpty());

            f.asMap().remove(1);
            assertTrue(f.asMap().isEmpty());

            f.asMap().remove(2);
            assertTrue(f.asList().isEmpty());

            f.asMap().remove(10);
            assertTrue(f.asMap().isEmpty());
        }
        @Test
        public void map_remove_outside_of_bound_does_nothing() {
            ListIsFunction f = new ListIsFunction();
            f.asList().add("aaa");
            f.asList().add("bbb");

            f.asMap().remove(-10);
            assertEquals(2, f.asList().size());

            f.asMap().remove(-1);
            assertEquals(2, f.asMap().size());

            f.asMap().remove(2);
            assertEquals(2, f.asList().size());

            f.asMap().remove(3);
            assertEquals(2, f.asMap().size());

            f.asMap().remove(10);
            assertEquals(2, f.asList().size());
        }
        @Test
        public void map_remove_last_works() {
            ListIsFunction f = new ListIsFunction();
            f.asList().add("aaa");
            f.asList().add("bbb");

            f.asMap().remove(1);
            assertEquals(1, f.asMap().size());
            assertFalse(f.asList().isEmpty());

            f.asMap().remove(0);
            assertEquals(0, f.asList().size());
            assertTrue(f.asMap().isEmpty());
        }
        @Test
        public void map_remove_in_the_middle_throws() {
            ListIsFunction f = new ListIsFunction();
            f.asList().add("aaa");
            f.asList().add("bbb");
            f.asList().add("ccc");

            try {
                f.asMap().remove(0);
                fail("No exception!");
            } catch(Exception e) {
                assertEquals(java.lang.IllegalArgumentException.class, e.getClass());
                assertEquals(3, f.asMap().size());
            }

            try {
                f.asMap().remove(1);
                fail("No exception!");
            } catch(Exception e) {
                assertEquals(java.lang.IllegalArgumentException.class, e.getClass());
                assertEquals(3, f.asList().size());
            }
        }
    }
    public static class MapPutTest {
        @Test
        public void map_put_strange_values() {
            ListIsFunction f = new ListIsFunction();

            f.asMap().put(0, "");
            assertEquals(1, f.asList().size());

            f.asMap().put(1, new Object());
            assertEquals(2, f.asMap().size());

            f.asMap().put(2, null);
            assertEquals(3, f.asMap().size());
        }
        @Test
        public void map_put_replace_strange_values() {
            ListIsFunction f = new ListIsFunction();

            assertNull(f.asMap().put(0, ""));
            assertEquals(1, f.asList().size());

            Object o = new Object();
            assertEquals("", f.asMap().put(0, o));
            assertEquals(1, f.asMap().size());

            assertEquals(o, f.asMap().put(0, null));
            assertEquals(1, f.asMap().size());

            assertNull(f.asMap().put(0, null));
            assertEquals(1, f.asList().size());
        }
        @Test
        public void map_put_bad_keys() {
            ListIsFunction f = new ListIsFunction();
            for (int i = 0; i < 5; ++i)
                f.asList().add(i);

            try {
                f.asMap().put(null, "in");
                fail("null key should result in exception");
            }
            catch(Exception e) {
                assertEquals(java.lang.NullPointerException.class, e.getClass());
            }

            try {
                f.asMap().put("2", "in");
                fail("non-integer key should result in exception");
            }
            catch(Exception e) {
                assertEquals(java.lang.ClassCastException.class, e.getClass());
            }

            try {
                f.asMap().put(new Object(), "in");
                fail("non-integer key should result in exception");
            }
            catch(Exception e) {
                assertEquals(java.lang.ClassCastException.class, e.getClass());
            }

            try {
                f.asMap().put(-1, "in");
                fail("negative key should result in exception");
            }
            catch(Exception e) {
                assertEquals(java.lang.IllegalArgumentException.class, e.getClass());
            }

            try {
                f.asMap().put(6, "in");
                fail("too big key should result in exception");
            }
            catch(Exception e) {
                assertEquals(java.lang.IllegalArgumentException.class, e.getClass());
            }

            assertEquals("[0, 1, 2, 3, 4]", f.asList().toString());
        }
    }
    public static class MapEntrySetTest {
        @Test
        public void entry_set_returns_correct_set() {
            ListIsFunction f = new ListIsFunction();
            f.asList().add("()");
            f.asList().add(1);
            f.asList().add('c');
            Set s_ret = f.asMap().entrySet();

            Set s_ex = new HashSet();
            s_ex.add(Map.entry(0, "()"));
            s_ex.add(Map.entry(1, 1));
            s_ex.add(Map.entry(2, 'c'));

            assertEquals(s_ex, s_ret);
            assertEquals("[(), 1, c]", f.asList().toString());
            assertEquals(3, f.asList().size());
        }
        @Test
        public void entry_set_iterator_next() {
            ListIsFunction f = new ListIsFunction();
            f.asList().add("()");
            f.asList().add(1);
            f.asList().add('c');
            Iterator it = f.asMap().entrySet().iterator();

            assertTrue(it.hasNext());
            assertEquals(it.next(), Map.entry(0, "()"));

            assertTrue(it.hasNext());
            assertEquals(it.next(), Map.entry(1, 1));

            assertTrue(it.hasNext());
            assertEquals(it.next(), Map.entry(2, 'c'));

            assertFalse(it.hasNext());
            try {
                it.next();
                fail("No exception!");
            } catch(Exception e) {
                assertEquals(java.util.NoSuchElementException.class, e.getClass());
            }

            assertEquals("[(), 1, c]", f.asList().toString());
            assertEquals(3, f.asList().size());
        }
        @Test
        public void entry_set_iterator_remove_first_throws() {
            ListIsFunction f = new ListIsFunction();
            f.asList().add("()");
            f.asList().add(1);
            f.asList().add('c');
            Iterator it = f.asMap().entrySet().iterator();

            try {
                it.remove();
                fail("No exception!");
            } catch(Exception e) {
                assertEquals(java.lang.IllegalStateException.class, e.getClass());
            }

            assertEquals("[(), 1, c]", f.asList().toString());
            assertEquals(3, f.asList().size());
        }
        @Test
        public void entry_set_iterator_remove_in_the_middle_throws() {
            ListIsFunction f = new ListIsFunction();
            f.asList().add("()");
            f.asList().add(1);
            f.asList().add('c');
            Iterator it = f.asMap().entrySet().iterator();

            it.next();
            it.next();

            try {
                it.remove();
                fail("No exception!");
            } catch(Exception e) {
                assertEquals(java.lang.IllegalStateException.class, e.getClass());
            }

            assertEquals("[(), 1, c]", f.asList().toString());
            assertEquals(3, f.asList().size());
        }
        @Test
        public void entry_set_iterator_remove_last_works() {
            ListIsFunction f = new ListIsFunction();
            f.asList().add("()");
            f.asList().add(1);
            f.asList().add('c');
            Iterator it = f.asMap().entrySet().iterator();

            it.next();
            it.next();
            it.next();
            it.remove();

            assertEquals("[(), 1]", f.asList().toString());
            assertEquals(2, f.asList().size());
        }
        @Test
        public void entry_set_iterator_remove_last_twice_throws() {
            ListIsFunction f = new ListIsFunction();
            f.asList().add("()");
            f.asList().add(1);
            f.asList().add('c');
            Iterator it = f.asMap().entrySet().iterator();

            it.next();
            it.next();
            it.next();
            it.remove();
            try {
                it.remove();
                fail("No exception!");
            } catch(Exception e) {
                assertEquals(java.lang.IllegalStateException.class, e.getClass());
            }

            assertEquals("[(), 1]", f.asList().toString());
            assertEquals(2, f.asList().size());
        }
    }
}
