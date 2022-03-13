import org.junit.Test;

import static org.junit.Assert.*;

public class LeonidDorochkoPhoneBookTest {

    @Test
    public void mutable_copy_test() {
        PhoneBook a = new PhoneBook().add("111000000");
        PhoneBook pb = new PhoneBook().add(a);
        assertEquals("{\n  {\n    111000000\n  }\n}\n", pb.toString());
        assertEquals("{\n  111000000\n}\n", a.toString());

        pb.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        assertEquals("{\n  {\n    111-000-000\n  }\n}\n", pb.toString());
        assertEquals("{\n  111000000\n}\n", a.toString());

        a.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        assertEquals("{\n  {\n    111-000-000\n  }\n}\n", pb.toString());
        assertEquals("{\n  111-000-000\n}\n", a.toString());

        a.changeFormat(PhoneBook.NumberFormat.DIGITS);
        assertEquals("{\n  {\n    111-000-000\n  }\n}\n", pb.toString());
        assertEquals("{\n  111000000\n}\n", a.toString());
    }

    @Test
    public void two_books_change_format_of_one() {
        PhoneBook book1 = new PhoneBook().add("111000111");
        PhoneBook book2 = new PhoneBook().add("222000222");
        assertEquals("{\n  111000111\n}\n", book1.toString());
        assertEquals("{\n  222000222\n}\n", book2.toString());
        book1.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        assertEquals("{\n  111-000-111\n}\n", book1.toString());
        assertEquals("{\n  222000222\n}\n", book2.toString());
    }

    @Test
    public void few_books_nested_then_change_format(){
        PhoneBook a = new PhoneBook(11).add("111111111");
        PhoneBook b = new PhoneBook(12).add(a);
        PhoneBook c = new PhoneBook(PhoneBook.NumberFormat.HYPHENED, 12).add(b);
        assertEquals("{\n  {\n    {\n      111-111-111\n    }\n  }\n}\n", c.toString());
        assertTrue(c.contains("111-111-111"));
        assertFalse(c.contains("111111111"));
        PhoneBook d = new PhoneBook(PhoneBook.NumberFormat.DIGITS, 20).add(c);
        assertEquals("{\n  {\n    {\n      {\n        111111111\n      }\n    }\n  }\n}\n", d.toString());
        assertTrue(d.contains("111111111"));
        assertFalse(d.contains("111-111-111"));
    }

    @Test
    public void add_myself_then_change_format () {
        PhoneBook book1 = new PhoneBook().add("111000111").add("222000222");
        PhoneBook book2 = new PhoneBook().add(book1);
        PhoneBook book3 = new PhoneBook().add(book2);
        book3.add(book3);
        PhoneBook book4 = new PhoneBook().add(book3).add(book2).add(book1);
        book4.add(book4);


        assertEquals("{\n  111000111\n  222000222\n}\n", book1.toString());
        assertEquals("{\n  {\n    111000111\n    222000222\n  }\n}\n", book2.toString());
        assertEquals("{\n  {\n    {\n      111000111\n      222000222\n    }\n  }\n  {\n    {\n      {\n        111000111\n        222000222\n      }\n    }\n  }\n}\n", book3.toString());
        assertEquals("{\n  {\n    {\n      {\n        111000111\n        222000222\n      }\n    }\n    {\n      {\n        {\n          111000111\n          222000222\n        }\n      }\n    }\n  }\n  {\n    {\n      111000111\n      222000222\n    }\n  }\n  {\n    111000111\n    222000222\n  }\n}\n", book4.toString());

        book1.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        assertEquals("{\n  111-000-111\n  222-000-222\n}\n", book1.toString());
        assertEquals("{\n  {\n    111000111\n    222000222\n  }\n}\n", book2.toString());
        assertEquals("{\n  {\n    {\n      111000111\n      222000222\n    }\n  }\n  {\n    {\n      {\n        111000111\n        222000222\n      }\n    }\n  }\n}\n", book3.toString());
        assertEquals("{\n  {\n    {\n      {\n        111000111\n        222000222\n      }\n    }\n    {\n      {\n        {\n          111000111\n          222000222\n        }\n      }\n    }\n  }\n  {\n    {\n      111000111\n      222000222\n    }\n  }\n  {\n    111000111\n    222000222\n  }\n}\n", book4.toString());

        book2.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        assertEquals("{\n  111-000-111\n  222-000-222\n}\n", book1.toString());
        assertEquals("{\n  {\n    111-000-111\n    222-000-222\n  }\n}\n", book2.toString());
        assertEquals("{\n  {\n    {\n      111000111\n      222000222\n    }\n  }\n  {\n    {\n      {\n        111000111\n        222000222\n      }\n    }\n  }\n}\n", book3.toString());
        assertEquals("{\n  {\n    {\n      {\n        111000111\n        222000222\n      }\n    }\n    {\n      {\n        {\n          111000111\n          222000222\n        }\n      }\n    }\n  }\n  {\n    {\n      111000111\n      222000222\n    }\n  }\n  {\n    111000111\n    222000222\n  }\n}\n", book4.toString());

        book3.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        assertEquals("{\n  111-000-111\n  222-000-222\n}\n", book1.toString());
        assertEquals("{\n  {\n    111-000-111\n    222-000-222\n  }\n}\n", book2.toString());
        assertEquals("{\n  {\n    {\n      111-000-111\n      222-000-222\n    }\n  }\n  {\n    {\n      {\n        111-000-111\n        222-000-222\n      }\n    }\n  }\n}\n", book3.toString());
        assertEquals("{\n  {\n    {\n      {\n        111000111\n        222000222\n      }\n    }\n    {\n      {\n        {\n          111000111\n          222000222\n        }\n      }\n    }\n  }\n  {\n    {\n      111000111\n      222000222\n    }\n  }\n  {\n    111000111\n    222000222\n  }\n}\n", book4.toString());

        book4.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        assertEquals("{\n  111-000-111\n  222-000-222\n}\n", book1.toString());
        assertEquals("{\n  {\n    111-000-111\n    222-000-222\n  }\n}\n", book2.toString());
        assertEquals("{\n  {\n    {\n      111-000-111\n      222-000-222\n    }\n  }\n  {\n    {\n      {\n        111-000-111\n        222-000-222\n      }\n    }\n  }\n}\n", book3.toString());
        assertEquals("{\n  {\n    {\n      {\n        111-000-111\n        222-000-222\n      }\n    }\n    {\n      {\n        {\n          111-000-111\n          222-000-222\n        }\n      }\n    }\n  }\n  {\n    {\n      111-000-111\n      222-000-222\n    }\n  }\n  {\n    111-000-111\n    222-000-222\n  }\n}\n", book4.toString());

        book3.changeFormat(PhoneBook.NumberFormat.DIGITS);
        assertEquals("{\n  111-000-111\n  222-000-222\n}\n", book1.toString());
        assertEquals("{\n  {\n    111-000-111\n    222-000-222\n  }\n}\n", book2.toString());
        assertEquals("{\n  {\n    {\n      111000111\n      222000222\n    }\n  }\n  {\n    {\n      {\n        111000111\n        222000222\n      }\n    }\n  }\n}\n", book3.toString());
        assertEquals("{\n  {\n    {\n      {\n        111-000-111\n        222-000-222\n      }\n    }\n    {\n      {\n        {\n          111-000-111\n          222-000-222\n        }\n      }\n    }\n  }\n  {\n    {\n      111-000-111\n      222-000-222\n    }\n  }\n  {\n    111-000-111\n    222-000-222\n  }\n}\n", book4.toString());

        book2.changeFormat(PhoneBook.NumberFormat.DIGITS);
        assertEquals("{\n  111-000-111\n  222-000-222\n}\n", book1.toString());
        assertEquals("{\n  {\n    111000111\n    222000222\n  }\n}\n", book2.toString());
        assertEquals("{\n  {\n    {\n      111000111\n      222000222\n    }\n  }\n  {\n    {\n      {\n        111000111\n        222000222\n      }\n    }\n  }\n}\n", book3.toString());
        assertEquals("{\n  {\n    {\n      {\n        111-000-111\n        222-000-222\n      }\n    }\n    {\n      {\n        {\n          111-000-111\n          222-000-222\n        }\n      }\n    }\n  }\n  {\n    {\n      111-000-111\n      222-000-222\n    }\n  }\n  {\n    111-000-111\n    222-000-222\n  }\n}\n", book4.toString());
    }
}
