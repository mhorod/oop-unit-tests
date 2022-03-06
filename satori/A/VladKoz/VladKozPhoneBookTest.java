import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VladKozPhoneBookTest {
    @Test
    public void addingElementsWithHostileIntent() {
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
        a.add("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        assertEquals("{\n  111-111-111\n}\n", a.toString());
    }

    @Test
    public void testCopy() {
        PhoneBook g1 = new PhoneBook().add("111000001").add("111000002").add("111000003");
        PhoneBook g2 = g1.copyBook();
        g2.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        assertEquals(3, g1.size());
        assertTrue(g1.equals(g2));
        g2.add("222-000-001");
        assertEquals(3, g1.size());
        assertEquals(4, g2.size());
        assertFalse(g1.equals(g2));
        assertTrue(g1.subsetOf(g2));
    }

    @Test
    public void EmptyAdd() {
        var pb = new PhoneBook(1);
        var empty = new PhoneBook(99);
        for (int i = 0; i < 10; i++) {
            pb.add(empty);
        }
        assertFalse(pb.isEmpty());
        assertEquals(pb.size(), 0);
        var notEmpty = new PhoneBook(1).add("111111111");
        pb.add(notEmpty);
        assertEquals(1, pb.size());
        assertEquals("""
                {
                  {
                  }
                  {
                    111111111
                  }
                }
                """, pb.toString());

        var obj = new PhoneBook();
        assertTrue(obj.isEmpty());
    }

    @Test
    public void isEmptyEpty() {
        var empty100proc = new PhoneBook(0);
        assertTrue(empty100proc.isEmpty());
        assertTrue(empty100proc.isFull());
        assertEquals(0, empty100proc.capacity());
        // RTE on 4'th test
        empty100proc.add("123");
        empty100proc.add(empty100proc);
        empty100proc.copyBook();
        assertFalse(empty100proc.contains("123"));
        assertFalse(empty100proc.contains(empty100proc));
        assertEquals(empty100proc, empty100proc);

        var pbWithEmpty = new PhoneBook(0);
        pbWithEmpty.add(pbWithEmpty);

        var pbEmpyADd = new PhoneBook();
        pbEmpyADd.add("011111111");
        pbEmpyADd.add("111-111-111");
        pbEmpyADd.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        pbEmpyADd.add("011-111-111");
        pbEmpyADd.add("111111111");
        assertTrue(pbEmpyADd.isEmpty());
    }

    private void RandomFillNum(PhoneBook b, int n, int seed) {
        var rand = new Random();
        rand.setSeed(seed);
        for (int i = 0; i < n; i++) { // SORRY MY FRIEND BUT you need to write getter on pb.format()
            if (b.format() == PhoneBook.NumberFormat.DIGITS) {
                b.add(randomTrioString(rand) +
                        randomTrioString(rand) +
                        randomTrioString(rand));
            } else {
                b.add(randomTrioString(rand) + "-"
                        + randomTrioString(rand) + "-"
                        + randomTrioString(rand));
            }
        }
    }

    private void RandomFillBooks(PhoneBook b, int n, int seed) {
        var rand = new Random();
        rand.setSeed(seed);
        for (int i = 0; i < n; i++) {
            var tmp = new PhoneBook();
            RandomFillNum(tmp, 2, seed + i);
            b.add(tmp);
        }
    }


    private String randomTrioString(Random rand) {
        return ((Integer) (rand.nextInt(899) + 100)).toString();
    }

    @Test
    public void testEquals() {
        {
            var pb1 = new PhoneBook(0);
            var pb2 = new PhoneBook(PhoneBook.NumberFormat.HYPHENED);
            assertEquals(pb1, pb2);
        }
        {
            var pb1 = new PhoneBook(null, -4);
            var pb2 = new PhoneBook();
            RandomFillNum(pb1, 12, 42);
            RandomFillNum(pb2, 12, 42);
            assertEquals(pb1, pb2);
            assertEquals(pb1.capacity(), pb1.size());
            assertEquals(pb1, pb2);
        }
        {
            var pb1 = new PhoneBook().add("111111111").add("222222222");
            var pb2 = new PhoneBook().add("222222222").add("111111111");
            RandomFillNum(pb1, 2, 42);
            RandomFillNum(pb2, 2, 42);
            assertEquals(pb1, pb2);
        }
        {
            var pb1 = new PhoneBook(PhoneBook.NumberFormat.HYPHENED);
            var pb2 = new PhoneBook();

            RandomFillBooks(pb1, 2, 42);
            var tmp = new PhoneBook();
            RandomFillNum(tmp, 2, 42);
            var tmp2 = new PhoneBook();
            RandomFillNum(tmp, 2, 43);
            pb2.add(tmp2);
            pb2.add(tmp);
            assertEquals(pb1, pb2);
        }
        {
            var pb1 = new PhoneBook();
            var pb2 = new PhoneBook(10);
            var pb3 = new PhoneBook(PhoneBook.NumberFormat.HYPHENED);
            var pb4 = new PhoneBook(PhoneBook.NumberFormat.DIGITS);
            var pb5 = new PhoneBook(PhoneBook.NumberFormat.HYPHENED, 10);
            RandomFillBooks(pb1, 11, 42);
            RandomFillBooks(pb2, 11, 42);
            RandomFillBooks(pb3, 11, 42);
            RandomFillBooks(pb4, 11, 42);
            RandomFillBooks(pb5, 11, 42);
            assertEquals(pb1, pb2);
            assertEquals(pb3, pb2);
            assertEquals(pb3, pb4);
            assertEquals(pb5, pb4);
            assertEquals(pb5, pb5);
        }
        {
            var pb2 = new PhoneBook(11);
            var pb5 = new PhoneBook(PhoneBook.NumberFormat.HYPHENED, 11);
            RandomFillBooks(pb2, 11, 42);
            RandomFillBooks(pb5, 11, 42);
            assertEquals(pb2, pb5);
        }
    }

    @Test
    public void testToString() {
        {
            var pbST = new PhoneBook();
            pbST.add(pbST);
            pbST.add(pbST);
            pbST.add(pbST);
            assertEquals("""
                    {
                      {
                      }
                      {
                        {
                        }
                      }
                      {
                        {
                        }
                        {
                          {
                          }
                        }
                      }
                    }
                    """, pbST.toString());
        }
    }

    @Test
    public void copyBook() {
        var pb42 = new PhoneBook().add("420000000");
        var pb43 = new PhoneBook().add("430000000");
        var pbpb43 = new PhoneBook().add(pb43);
        var book = new PhoneBook(42).add(pb42).add(pb43).add(pbpb43);
        // { 42 43 {43} }
        var copyPb = book.copyBook();
        assertEquals(copyPb, book);

        pb42.add("420000000");
        pb43.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        assertEquals(copyPb, book);

        book.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        assertEquals(copyPb, book);
        assertEquals(pb43.add("111111111").size(), 1);
    }

    @Test
    public void size() {
        var ofiara = new PhoneBook();
        for (int i = 0; i < 15; i++) {
            ofiara.add(new PhoneBook());
        }
        assertEquals(0, ofiara.size());

        for (int i = 0; i < 15; i++) {
            ofiara.add(new PhoneBook().add("123123123"));
        }
        assertEquals(1, ofiara.size());
    }

    @Test
    public void isEmpty() {
        var bookE = new PhoneBook();
        var bookN = new PhoneBook().add("123123123");
        var bookPB = new PhoneBook().add(bookE);
        assertEquals(bookE.size(), 0);
        assertEquals(bookN.size(), 1);
        assertEquals(bookPB.size(), 0);
        assertTrue(bookE.isEmpty());
        assertFalse(bookPB.isEmpty());
    }

    @Test
    public void elementOf() {
        PhoneBook g0 = new PhoneBook().add("111000001").add("111000003");
        PhoneBook g1 = new PhoneBook().add("111000001").add("111000002").add("111000003");
        PhoneBook g2 = new PhoneBook().add("222000001").add("222000002").add("222000003");
        PhoneBook pb1 = new PhoneBook(7).add(g1).add(g2);
        PhoneBook pb2 = new PhoneBook().add(g2).add(g1);
        pb1.add(g0);
        pb2.add(g0);
        assertFalse(pb1.equals(pb2));
    }
}