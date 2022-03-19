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
        public void clear_works_correctly() {
            ListIsFunction f = new ListIsFunction();
            List l = f.asList();
            Map m = f.asMap();

            l.add(1);
            assertFalse(l.isEmpty());
            assertFalse(m.isEmpty());

            m.clear();
            assertTrue(l.isEmpty());
            assertTrue(m.isEmpty());

            m.put(0, 1);
            assertFalse(l.isEmpty());
            assertFalse(m.isEmpty());

            l.clear();
            assertTrue(l.isEmpty());
            assertTrue(m.isEmpty());
        }
        @Test(timeout = 250)
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
        public void map_remove_out_of_bounds_does_nothing() {
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
                fail("map can't remove in the middle");
            } catch(Exception e) {
                assertEquals(java.lang.IllegalArgumentException.class, e.getClass());
                assertEquals("java.lang.IllegalArgumentException", e.toString());

                assertEquals(3, f.asMap().size());
            }

            try {
                f.asMap().remove(1);
                fail("map can't remove in the middle");
            } catch(Exception e) {
                assertEquals(java.lang.IllegalArgumentException.class, e.getClass());
                assertEquals("java.lang.IllegalArgumentException", e.toString());

                assertEquals(3, f.asList().size());
            }
        }
    }
    public static class MapPutTest {
        @Test
        public void map_put_works_with_strange_values() {
            ListIsFunction f = new ListIsFunction();

            f.asMap().put(0, "");
            assertEquals(1, f.asList().size());

            f.asMap().put(1, new Object());
            assertEquals(2, f.asMap().size());

            f.asMap().put(2, null);
            assertEquals(3, f.asMap().size());
        }
        @Test
        public void map_put_replace_works_with_strange_values() {
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
            } catch (Exception e) {
                assertEquals(java.lang.NullPointerException.class, e.getClass());
                assertEquals("java.lang.NullPointerException", e.toString());

                assertEquals(5, f.asList().size());
            }

            try {
                f.asMap().put("2", "in");
                fail("non-integer key should result in exception");
            } catch (Exception e) {
                assertEquals(java.lang.ClassCastException.class, e.getClass());
                assertEquals("java.lang.ClassCastException", e.toString());

                assertEquals(5, f.asMap().size());
            }

            try {
                f.asMap().put(new Object(), "in");
                fail("non-integer key should result in exception");
            } catch (Exception e) {
                assertEquals(java.lang.ClassCastException.class, e.getClass());
                assertEquals("java.lang.ClassCastException", e.toString());

                assertEquals(5, f.asList().size());
            }

            try {
                f.asMap().put(-1, "in");
                fail("negative key should result in exception");
            } catch (Exception e) {
                assertEquals(java.lang.IllegalArgumentException.class, e.getClass());
                assertEquals("java.lang.IllegalArgumentException", e.toString());

                assertEquals(5, f.asMap().size());
            }

            try {
                f.asMap().put(6, "in");
                fail("too big key should result in exception");
            } catch (Exception e) {
                assertEquals(java.lang.IllegalArgumentException.class, e.getClass());
                assertEquals("java.lang.IllegalArgumentException", e.toString());

                assertEquals(5, f.asList().size());
            }

            assertEquals("[0, 1, 2, 3, 4]", f.asList().toString());
        }
    }
    public static class MapEntrySetTest {
        @Test
        public void entry_set_is_a_view() {
            ListIsFunction f = new ListIsFunction();
            Set es = f.asMap().entrySet();
            Set ks = f.asMap().keySet();

            assertEquals(0, es.size());
            assertTrue(es.isEmpty());
            assertEquals("[]", es.toString());

            assertEquals(0, ks.size());
            assertTrue(ks.isEmpty());
            assertEquals("[]", ks.toString());

            f.asList().add(13);

            assertEquals(1, es.size());
            assertFalse(es.isEmpty());
            assertEquals("[0=13]", es.toString());

            assertEquals(1, ks.size());
            assertFalse(ks.isEmpty());
            assertEquals("[0]", ks.toString());

            f.asList().add("test");

            assertEquals(2, es.size());
            assertFalse(es.isEmpty());
            assertEquals("[0=13, 1=test]", es.toString());

            assertEquals(2, ks.size());
            assertFalse(ks.isEmpty());
            assertEquals("[0, 1]", ks.toString());

            f.asList().clear();

            assertEquals(0, es.size());
            assertTrue(es.isEmpty());
            assertEquals("[]", es.toString());

            assertEquals(0, ks.size());
            assertTrue(ks.isEmpty());
            assertEquals("[]", ks.toString());
        }
        @Test
        public void entry_set_returns_correct_set() {
            ListIsFunction f = new ListIsFunction();
            f.asList().add("()");
            f.asList().add(1);
            f.asList().add('c');
            Set s_ret = f.asMap().entrySet();

            Set s_ex = new HashSet();
            s_ex.add(new AbstractMap.SimpleEntry(0, "()"));
            s_ex.add(new AbstractMap.SimpleEntry(1, 1));
            s_ex.add(new AbstractMap.SimpleEntry(2, 'c'));

            assertEquals(s_ex, s_ret);
            assertEquals("[(), 1, c]", f.asList().toString());
            assertEquals(3, f.asList().size());
        }
        @Test
        public void entry_set_works_with_strange_values() {
            ListIsFunction f = new ListIsFunction();
            Set s = f.asMap().entrySet();

            f.asList().add(null);
            f.asList().add("");

            assertEquals("[0=null, 1=]", s.toString());
            assertEquals(2, s.size());

            f.asList().add(new Object());
            assertEquals(3, s.size());
        }
    }
    public static class MapEntrySetIteratorTest {
        @Test
        public void entry_set_iterator_next() {
            ListIsFunction f = new ListIsFunction();
            f.asList().add(-1);
            f.asList().add(null);
            Iterator it = f.asMap().entrySet().iterator();

            assertTrue(it.hasNext());
            assertEquals(it.next(), new AbstractMap.SimpleEntry(0, -1));

            assertTrue(it.hasNext());
            assertEquals(it.next(), new AbstractMap.SimpleEntry(1, null));

            assertFalse(it.hasNext());
            try {
                it.next();
                fail("there is no next element");
            } catch(Exception e) {
                assertEquals(java.util.NoSuchElementException.class, e.getClass());
                assertEquals("java.util.NoSuchElementException", e.toString());
            }

            assertEquals("[-1, null]", f.asList().toString());
            assertEquals(2, f.asList().size());
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
                fail("remove() has to be called after next()");
            } catch(Exception e) {
                assertEquals(java.lang.IllegalStateException.class, e.getClass());
                assertEquals("java.lang.IllegalStateException", e.toString());
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
                fail("map iterator can't remove in the middle");
            } catch(Exception e) {
                assertEquals(java.lang.IllegalStateException.class, e.getClass());
                assertEquals("java.lang.IllegalStateException", e.toString());
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
                fail("map iterator can't remove twice");
            } catch(Exception e) {
                assertEquals(java.lang.IllegalStateException.class, e.getClass());
                assertEquals("java.lang.IllegalStateException", e.toString());
            }

            assertEquals("[(), 1]", f.asList().toString());
            assertEquals(2, f.asList().size());
        }
    }
}
