import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class MichalHorodeckiPhoneBookTest
{
    // Test if different constructors correctly assign default values
    public static class ConstructorTest
    {
        @Test
        public void by_default_book_has_capacity_ten_and_format_digits()
        {
            PhoneBook pb = new PhoneBook();
            for (int i = 0; i < 10; i++)
                pb.add("12345678" + i);
            assertEquals(10, pb.size());
            assertTrue(pb.isFull());
        }

        @Test
        public void by_default_book_has_capacity_of_ten()
        {
            PhoneBook pb = new PhoneBook(PhoneBook.NumberFormat.HYPHENED);
            for (int i = 0; i < 10; i++)
                pb.add("123-456-78" + i);
            assertEquals(10, pb.size());
            assertTrue(pb.isFull());
        }

        @Test
        public void by_default_book_has_format_digits()
        {
            PhoneBook pb = new PhoneBook(5);
            for (int i = 0; i < 5; i++)
                pb.add("12345678" + i);
            assertEquals(5, pb.size());
            assertTrue(pb.isFull());
        }

        @Test
        public void constructor_called_with_invalid_data_uses_default_values()
        {
            PhoneBook pb = new PhoneBook(null, -5);
            assertEquals(10, pb.capacity());
            pb.add("123456789");
            assertFalse(pb.isEmpty());
        }
    }

    // Test inclusion relations on phone books and numbers
    public static class InclusionTest
    {
        @Test
        public void book_contains_valid_numbers_that_were_added()
        {
            PhoneBook pb = new PhoneBook();
            pb.add("123456789");
            pb.add("456789012");
            assertTrue(pb.contains("123456789"));
            assertTrue(pb.contains("456789012"));
        }

        @Test
        public void book_does_not_contain_numbers_in_different_format()
        {
            PhoneBook pb = new PhoneBook();
            pb.add("123456789");
            pb.add("456789012");
            assertFalse(pb.contains("123-456-789"));
            assertFalse(pb.contains("456-789-012"));
        }

        @Test
        public void book_with_changed_format_contains_numbers_in_changed_format()
        {
            PhoneBook pb = new PhoneBook();
            pb.add("123456789");
            pb.add("456789012");
            pb.changeFormat(PhoneBook.NumberFormat.HYPHENED);
            assertTrue(pb.contains("123-456-789"));
            assertTrue(pb.contains("456-789-012"));
        }

        @Test
        public void book_does_not_contain_null()
        {
            PhoneBook pb = new PhoneBook().add("123456789");
            assertFalse(pb.contains((String) null));
        }

        @Test
        public void book_containing_book_of_numbers_contains_those_numbers()
        {
            PhoneBook numbers = new PhoneBook().add("123456789").add("456789012");
            PhoneBook books = new PhoneBook().add(numbers);
            assertTrue(books.contains("123456789"));
            assertTrue(books.contains("456789012"));
        }

        @Test
        public void book_containing_book_of_numbers_contains_those_numbers_in_changed_format()
        {
            PhoneBook numbers = new PhoneBook().add("123456789").add("456789012");
            PhoneBook books = new PhoneBook().add(numbers);
            books.changeFormat(PhoneBook.NumberFormat.HYPHENED);
            assertFalse(books.contains("123456789"));
            assertFalse(books.contains("456789012"));
            assertTrue(books.contains("123-456-789"));
            assertTrue(books.contains("456-789-012"));
        }

        @Test
        public void book_containing_book_of_numbers_contains_those_numbers_in_adjusted_format()
        {
            PhoneBook numbers = new PhoneBook().add("123456789").add("456789012");
            PhoneBook books = new PhoneBook(PhoneBook.NumberFormat.HYPHENED).add(numbers);
            assertFalse(books.contains("123456789"));
            assertFalse(books.contains("456789012"));
            assertTrue(books.contains("123-456-789"));
            assertTrue(books.contains("456-789-012"));
        }

        @Test
        public void book_is_not_element_of_null()
        {
            PhoneBook pb = new PhoneBook();
            assertFalse(pb.elementOf(null));
        }

        @Test
        public void book_is_element_of_book_it_was_added_to()
        {
            PhoneBook element = new PhoneBook();
            PhoneBook container = new PhoneBook();
            container.add(element);
            assertTrue(element.elementOf(container));
        }

        @Test
        public void book_does_not_contain_itself()
        {
            PhoneBook pb = new PhoneBook();
            pb.add(pb);
            assertFalse(pb.contains(pb));
        }

        @Test
        public void book_is_not_subset_of_null()
        {
            assertFalse(new PhoneBook().subsetOf(null));
        }

        @Test
        public void book_is_not_superset_of_null()
        {
            assertFalse(new PhoneBook().supersetOf(null));
        }

        @Test
        public void empty_book_is_its_subset_and_superset()
        {
            PhoneBook empty = new PhoneBook();
            assertTrue(empty.supersetOf(empty));
            assertTrue(empty.subsetOf(empty));
        }


        @Test
        public void empty_book_is_subset_of_non_empty_book()
        {
            PhoneBook subset = new PhoneBook();
            PhoneBook superset = new PhoneBook().add("123456789");
            assertTrue(subset.subsetOf(superset));
        }

        @Test
        public void non_empty_book_is_superset_of_empty_book()
        {
            PhoneBook subset = new PhoneBook();
            PhoneBook superset = new PhoneBook().add("123456789");
            assertTrue(superset.supersetOf(subset));
        }

        @Test
        public void format_is_ignored_when_checking_subset()
        {
            PhoneBook subset = new PhoneBook().add("123456789");
            subset.changeFormat(PhoneBook.NumberFormat.HYPHENED);
            PhoneBook superset = new PhoneBook().add("123456789").add("456789012");
            assertTrue(subset.subsetOf(superset));
        }

        @Test
        public void format_is_ignored_when_checking_superset()
        {
            PhoneBook subset = new PhoneBook().add("123456789");
            subset.changeFormat(PhoneBook.NumberFormat.HYPHENED);
            PhoneBook superset = new PhoneBook().add("123456789").add("456789012");
            assertTrue(superset.supersetOf(subset));
        }

        @Test
        public void set_inclusion_is_not_recursive()
        {
            PhoneBook almost_superset = new PhoneBook().add(new PhoneBook().add("123456789"));
            PhoneBook almost_subset = new PhoneBook().add("123456789");
            assertFalse(almost_subset.subsetOf(almost_superset));
            assertFalse(almost_superset.supersetOf(almost_subset));
        }

        @Test
        public void containing_books_is_not_recursive()
        {
            PhoneBook pb = new PhoneBook().add(new PhoneBook().add(new PhoneBook()).add("123456789"));
            PhoneBook almost_contained = new PhoneBook().add("123456789");
            assertFalse(pb.contains(almost_contained));
        }

        @Test
        public void nested_inclusion_test()
        {
            PhoneBook a = new PhoneBook().add("123456789");
            PhoneBook b = new PhoneBook().add("223456789");
            PhoneBook c = new PhoneBook().add("323456789");
            PhoneBook x = new PhoneBook().add("423456789");
            PhoneBook y = new PhoneBook().add("523456789");

            PhoneBook abc = new PhoneBook().add(a).add(b).add(c);
            PhoneBook xy = new PhoneBook().add(x).add(y);
            PhoneBook first = new PhoneBook().add(abc).add(x);
            PhoneBook second = new PhoneBook().add(abc).add(xy);

            assertFalse(first.subsetOf(second));
            assertFalse(second.supersetOf(first));
        }
    }

    // Test conditions of two books being equal
    public static class EqualityTest
    {
        @Test
        public void two_newly_created_books_are_equal()
        {
            assertEquals(new PhoneBook(), new PhoneBook());
        }

        @Test
        public void books_containing_the_same_numbers_in_the_same_format_are_equal()
        {
            PhoneBook first = new PhoneBook();
            PhoneBook second = new PhoneBook();
            first.add("123456789");
            first.add("456789012");

            second.add("123456789");
            second.add("456789012");
            assertEquals(first, second);
        }

        @Test
        public void books_containing_the_same_numbers_in_different_format_are_equal()
        {
            PhoneBook digits = new PhoneBook();
            PhoneBook hyphened = new PhoneBook(PhoneBook.NumberFormat.HYPHENED);
            digits.add("123456789");
            digits.add("456789012");

            hyphened.add("123-456-789");
            hyphened.add("456-789-012");
            assertEquals(digits, hyphened);
        }

        @Test
        public void books_of_different_capacity_are_equal_when_contain_equal_numbers()
        {
            PhoneBook small = new PhoneBook(4);
            PhoneBook big = new PhoneBook(10);
            small.add("123456789");
            big.add("123456789");
            assertEquals(small, big);
        }

        @Test
        public void book_is_not_equal_to_null()
        {
            assertNotEquals(null, new PhoneBook());
        }

        @Test
        public void containing_equal_numbers_in_subbooks_is_insufficient_to_be_equal()
        {
            PhoneBook pb1 = new PhoneBook().add("123456789");
            PhoneBook pb2 = new PhoneBook().add(new PhoneBook().add("123456789"));
            assertNotEquals(pb1, pb2);
        }
    }

    // Test if serialization to strings works as expected
    public static class ToStringTest
    {

        @Test
        public void empty_book_to_string()
        {
            String expected = "{\n}\n";
            String actual = new PhoneBook().toString();
            assertEquals(expected, actual);
        }

        @Test
        public void book_containing_empty_book_to_string()
        {
            String expected = "{\n  {\n  }\n}\n";
            String actual = new PhoneBook().add(new PhoneBook()).toString();
            assertEquals(expected, actual);
        }

        @Test
        public void book_with_two_empty_books_added_to_string()
        {
            String expected = "{\n  {\n  }\n}\n";
            String actual = new PhoneBook().add(new PhoneBook()).add(new PhoneBook()).toString();
            assertEquals(expected, actual);
        }


        @Test
        public void book_string_contains_numbers_in_current_format()
        {
            String expected = "{\n  123-456-789\n}\n";
            PhoneBook pb = new PhoneBook().add("123456789");
            pb.changeFormat(PhoneBook.NumberFormat.HYPHENED);
            assertEquals(expected, pb.toString());
        }

        @Test
        public void subbooks_string_contains_numbers_in_current_format()
        {
            String expected = "{\n  {\n    123-456-789\n  }\n}\n";
            PhoneBook pb = new PhoneBook().add(new PhoneBook().add("123456789"));
            pb.changeFormat(PhoneBook.NumberFormat.HYPHENED);
            assertEquals(expected, pb.toString());
        }

        @Test
        public void book_of_books_to_string()
        {
            String expected = "{\n  {\n    123456789\n  }\n}\n";
            PhoneBook inner = new PhoneBook().add("123456789");
            PhoneBook outer = new PhoneBook().add(inner);
            assertEquals(expected, outer.toString());
        }
    }

    // Test conditions of being (not) empty
    public static class EmptinessTest
    {
        @Test
        public void newly_constructed_book_is_empty()
        {
            PhoneBook pb = new PhoneBook();
            assertTrue(pb.isEmpty());
            assertEquals(0, pb.size());
        }

        @Test
        public void book_containing_empty_book_is_not_empty()
        {
            PhoneBook pb = new PhoneBook();
            pb.add(new PhoneBook());
            assertFalse(pb.isEmpty());
        }

        @Test
        public void book_containing_empty_book_has_size_of_zero()
        {
            PhoneBook pb = new PhoneBook();
            pb.add(new PhoneBook());
            assertEquals(0, pb.size());
        }

        @Test
        public void book_with_capacity_of_zero_is_not_empty_after_adding_empty_book()
        {
            PhoneBook pb = new PhoneBook(0);
            pb.add(new PhoneBook());
            assertFalse(pb.isEmpty());
        }

        @Test
        public void book_with_capacity_of_zero_is_empty_after_adding_number()
        {
            PhoneBook pb = new PhoneBook(0);
            pb.add("123456789");
            assertTrue(pb.isEmpty());
        }
    }

    // Test how adding various data affects size of a book
    public static class AddingAndCheckingSizeTest
    {
        @Test
        public void adding_valid_number_increments_size()
        {
            PhoneBook pb = new PhoneBook();
            pb.add("123456789");
            pb.changeFormat(PhoneBook.NumberFormat.HYPHENED);
            pb.add("456-789-012");
            assertEquals(2, pb.size());
        }

        @Test
        public void adding_number_of_different_format_does_not_change_size()
        {
            PhoneBook pb = new PhoneBook();
            pb.add("123-456-789");
            pb.changeFormat(PhoneBook.NumberFormat.HYPHENED);
            pb.add("123456789");
            assertEquals(0, pb.size());
        }

        @Test
        public void adding_invalid_number_does_not_change_size()
        {
            PhoneBook pb = new PhoneBook();
            pb.add("123");          // Number is too short
            pb.add("012345678");    // Number cannot start with 0
            pb.add("");             // Number cannot be empty
            pb.add("falowana blaszka"); // not a number
            pb.add((String) null);  // Cannot add null

            pb.changeFormat(PhoneBook.NumberFormat.HYPHENED);
            pb.add("1-234-56789");  // Hyphens in invalid places
            pb.add("-1-2-3-4-5-6-7-8-9-"); // Invalid number of hyphens
            pb.add("12-45-789");  // Invalid  number of digits
            pb.add("012-345-678"); // Hyphened number cannot start with 0

            assertEquals(0, pb.size());
        }

        @Test
        public void adding_existing_number_does_not_change_size()
        {
            PhoneBook pb = new PhoneBook();
            pb.add("123456789");
            pb.add("456789012");
            pb.add("123456789");
            assertEquals(2, pb.size());
        }

        @Test
        public void adding_existing_number_after_changing_format_does_not_change_size()
        {
            PhoneBook pb = new PhoneBook();
            pb.add("123456789");
            pb.changeFormat(PhoneBook.NumberFormat.HYPHENED);
            pb.add("123-456-789");
            assertEquals(1, pb.size());
        }

        @Test
        public void adding_number_to_book_of_books_does_not_change_size()
        {
            PhoneBook pb = new PhoneBook();
            pb.add(new PhoneBook().add("456789012"));
            pb.add("123456789");
            assertEquals(1, pb.size());
        }

        @Test
        public void adding_book_to_book_of_numbers_does_not_change_size()
        {
            PhoneBook pb = new PhoneBook();
            pb.add("123456789");
            pb.add(new PhoneBook().add("456789012"));
            assertEquals(1, pb.size());
        }

        @Test
        public void adding_book_after_adding_invalid_number_changes_size()
        {
            PhoneBook pb = new PhoneBook();
            pb.add("012345678");
            pb.add("123-456-789");
            pb.add((String) null);
            pb.add(new PhoneBook().add("456789012").add("123456789").add("111222333"));
            assertEquals(3, pb.size());
        }

        @Test
        public void adding_book_of_different_format_adds_it_with_changed_format()
        {
            PhoneBook books = new PhoneBook();
            PhoneBook hyphened = new PhoneBook(PhoneBook.NumberFormat.HYPHENED);
            hyphened.add("123-456-789");
            books.add(hyphened);
            assertTrue(books.contains(hyphened));
            assertTrue(books.contains("123456789"));
        }

        @Test
        public void adding_book_which_size_fills_capacity_changes_changes_size_to_full()
        {
            PhoneBook books = new PhoneBook(3);
            PhoneBook large = new PhoneBook().add("123456789").add("456789012").add("111222333");
            books.add(large);
            assertEquals(3, books.size());
            assertTrue(books.contains(large));
            assertTrue(books.isFull());
        }

        @Test
        public void adding_book_that_would_exceed_capacity_does_not_change_size()
        {
            PhoneBook books = new PhoneBook(2);
            PhoneBook large = new PhoneBook().add("123456789").add("456789012").add("111222333");
            books.add(large);
            assertEquals(0, books.size());
            assertFalse(books.contains(large));
        }

        @Test
        public void size_of_book_of_books_is_sum_of_sizes_of_contained_books()
        {
            PhoneBook pb1 = new PhoneBook().add("123456789").add("456789012");
            PhoneBook pb2 = new PhoneBook().add("111222333");
            PhoneBook books = new PhoneBook().add(pb1).add(pb2);
            assertEquals(3, books.size());
        }

        @Test
        public void adding_number_after_adding_null_book_increments_size()
        {
            PhoneBook pb = new PhoneBook();
            pb.add((PhoneBook) null);
            pb.add("123456789");
            assertEquals(1, pb.size());
        }

        @Test
        public void adding_existing_book_does_not_change_size()
        {
            PhoneBook books = new PhoneBook();
            PhoneBook pb = new PhoneBook().add("123456789");
            books.add(pb).add(pb);
            assertEquals(1, pb.size());
        }

        @Test
        public void can_add_at_most_ten_numbers()
        {
            PhoneBook pb = new PhoneBook(20);
            for (int i = 0; i < 20; i++)
                pb.add(String.format("1234567%02d", i));
            assertEquals(10, pb.size());
        }

        @Test
        public void can_add_at_most_ten_books()
        {
            PhoneBook pb = new PhoneBook(20);
            for (int i = 0; i < 10; i++)
                pb.add(pb);
            // pb contains ten books without any numbers
            // If the below was added the size would equal ten
            pb.add(new PhoneBook().add("123456789"));
            assertEquals(0, pb.size());
        }

        @Test
        public void capacity_can_exceed_ten()
        {
            PhoneBook pb = new PhoneBook(32);
            PhoneBook content = new PhoneBook().add("123456789");
            pb.add(content);
            for (int i = 0; i < 5; i++) // Double the size with each iteration
                pb.add(pb);
            assertEquals(32, pb.size());
        }
    }

    // Test if objects are copied accordingly
    public static class CopyingTest
    {
        @Test
        public void changing_added_subbook_does_not_change_size()
        {
            PhoneBook numbers = new PhoneBook().add("123456789");
            PhoneBook pb = new PhoneBook().add(numbers);
            PhoneBook added = new PhoneBook();
            pb.add(added);
            added.add("456789012");
            assertEquals(1, pb.size());
            assertFalse(pb.contains(added));
        }

        @Test
        public void adding_number_to_copy_does_not_change_original_book()
        {
            PhoneBook original = new PhoneBook().add("123456789");
            PhoneBook copy = original.copyBook();
            String number = "456789012";
            copy.add(number);

            assertNotEquals(original, copy);
            assertEquals(1, original.size());
            assertFalse(original.contains(number));
        }

        @Test
        public void changing_format_of_copy_does_not_change_format_of_original_book()
        {
            PhoneBook original = new PhoneBook().add("123456789");
            PhoneBook copy = original.copyBook();
            String number = "456-789-012"; // Different format than original
            copy.changeFormat(PhoneBook.NumberFormat.HYPHENED);

            copy.add(number);
            original.add(number); // The number is hyphened and shouldn't be added to original

            assertNotEquals(original, copy);
            assertFalse(original.contains(number));
            assertEquals(1, original.size());
        }

        @Test
        public void copy_of_book_of_books_is_independent_of_original()
        {
            PhoneBook original = new PhoneBook().add(new PhoneBook());
            PhoneBook copy = original.copyBook();
            copy.add(new PhoneBook().add("123456789"));

            assertNotEquals(original, copy);
            assertEquals(0, original.size());
        }

        @Test
        public void book_of_numbers_is_equal_to_its_copy()
        {
            PhoneBook numbers = new PhoneBook().add("123456789").add("456789012");
            PhoneBook copy = numbers.copyBook();
            assertEquals(numbers, copy);
        }

        @Test
        public void book_of_books_is_equal_to_its_copy()
        {
            PhoneBook pb1 = new PhoneBook().add("123456789");
            PhoneBook pb2 = new PhoneBook().add("456789012");
            PhoneBook books = new PhoneBook().add(pb1).add(pb2);
            PhoneBook copy = books.copyBook();
            assertEquals(books, copy);
        }
    }

}
