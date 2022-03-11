import java.util.Arrays;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JakubOskwarekRepartitionedOfficialATest {

    @Test
    public void a_contains_all_numbers_added_in_separate_statements() {
        a a = new a();
        a.a(1);
        a.a(2);
        a.a(3);
        assertEquals("[1, 2, 3]", String.valueOf(a));
    }

    @Test
    public void a_contains_all_numbers_added_via_method_chaining() {
        a a = new a().a(1).a(2).a(3);
        assertEquals("[1, 2, 3]", String.valueOf(a));
    }

    @Test
    public void single_a_prints_sorted() {
        a a = new a();
        a.a(1);
        a.a(3);
        a.a(2);
        assertEquals("[1, 2, 3]", String.valueOf(a));
    }

    @Test
    public void double_a_prints_in_original_order() {
        a a = new a();
        a.a(1).a(3).a(2);
        assertEquals("[1, 3, 2]", String.valueOf(a.a));
    }

    @Test
    public void elements_can_be_popped_from_back_in_reversed_insertion_order() {
        a a = new a();
        a.a(1).a(5).a(2);
        assertEquals("[1, 2, 5]", String.valueOf(a));
        assertEquals("[1, 5, 2]", String.valueOf(a.a));
        assertEquals("2", String.valueOf(a.a())); // popping here
        assertEquals("[1, 5]", String.valueOf(a));
        assertEquals("[1, 5]", String.valueOf(a.a));
        assertEquals("5", String.valueOf(a.a())); // popping here
    }

    @Test
    public void elements_popped_from_back_can_be_pushed_to_front() {
        a a = new a();
        a.a(1).a(5).a(2).a(4).a(3);
        assertEquals("[1, 2, 3, 4, 5]", String.valueOf(a));
        assertEquals("[1, 5, 2, 4, 3]", String.valueOf(a.a));
        a.a(a.a()); // this line is the essential one
        assertEquals("[1, 2, 3, 4, 5]", String.valueOf(a));
        assertEquals("[3, 1, 5, 2, 4]", String.valueOf(a.a));
    }

    @Test
    public void reference_returned_from_pushing_to_front_prints_sorted() {
        a a = new a();
        a.a(1).a(5);
        assertEquals("[1, 3, 5]", a.a(3).toString());
    }

    @Test
    public void reference_returned_from_pushing_popped_element_to_front_prints_in_original_order() {
        a a = new a();
        a.a(1).a(5).a(3);
        assertEquals("[3, 1, 5]", a.a(a.a()).toString());
    }

    @Test
    public void following_the_nested_a_does_not_change_the_result_of_pushing_to_back() {
        a a = new a();
        a.a(1).a(1).a(2).a(1).a(1);
        assertEquals("[1, 1, 2, 1, 1]", String.valueOf(a.a));
        a.a.a(3).a.a(1).a(1).a.a.a(4);
        assertEquals("[1, 1, 2, 1, 1, 3, 1, 1, 4]", String.valueOf(a.a));
        a = a.a; // this line is the essential one
        a.a(1).a(1).a.a.a(5);
        assertEquals("[1, 1, 2, 1, 1, 3, 1, 1, 4, 1, 1, 5]", String.valueOf(a));
        assertEquals("[1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 4, 5]", String.valueOf(a.a)); // now double `a` prints sorted
    }

    @Test
    public void elements_can_be_dumped_to_an_array_of_ints() {
        a a = new a();
        a.a(1).a(5).a(2).a(4).a(3);
        assertEquals("[3, 4, 2, 5, 1]", Arrays.toString(new int[]{a.a(), a.a(), a.a(), a.a(), a.a()}));
    }

    @Test
    public void elements_can_be_pushed_to_back_at_every_nesting_level() {
        a a = new a();
        a.a.a.a.a.a.a.a.a.a(1);
        a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a(3);
        a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a(2);
        assertEquals("[1, 2, 3]", String.valueOf(a));
        assertEquals("[1, 3, 2]", String.valueOf(a.a));
    }

    @Test
    public void as_nest_indefinitely() {
        a a = new a();
        a.a(1).a(3).a(2);
        for(int i = 0; i < 1000000; i++) {
            assertEquals("[1, 2, 3]", String.valueOf(a));
            a = a.a;
            assertEquals("[1, 3, 2]", String.valueOf(a));
            a = a.a;
        }
    }

    @Test
    public void number_of_intermediate_as_does_not_matter_when_pushing_to_back() {
        a a = new a();
        assertEquals("[1, 2, 3]", String.valueOf(a.a(1).a(3).a(2)));
        a = new a();
        assertEquals("[1, 3, 2]", String.valueOf(a.a(1).a(3).a(2).a));
        a = new a();
        assertEquals("[1, 2, 3]", String.valueOf(a.a.a(1).a(3).a(2)));
        a = new a();
        assertEquals("[1, 2, 3]", String.valueOf(a.a.a(1).a.a.a(3).a.a.a.a.a(2)));
    }

    @Test
    public void operations_can_be_nested() {
        a a = new a();
        assertEquals("[1, 2, 3, 4, 5]", String.valueOf(a.a(1).a(2).a(3).a(4).a(5)));
        assertEquals("[5, 1, 2, 3, 4]", String.valueOf(a.a(a.a())));
        assertEquals("[4, 5, 1, 2, 3]", String.valueOf(a.a(a.a())));
        assertEquals("[1, 2, 3, 4, 5, 6]", String.valueOf(a.a(6)));
        assertEquals("[6, 4, 5, 1, 2, 3]", String.valueOf(a.a(a.a())));
    }

    @Test
    public void operations_can_be_sequenced_with_nesting() {
        a a = new a();
        assertEquals("[1, 2, 3, 4, 5]", String.valueOf(a.a(1).a(2).a(3).a(4).a(5)));
        assertEquals("[5, 1, 2, 3, 4]", String.valueOf(a.a.a(a.a())));
        assertEquals("[4, 5, 1, 2, 3]", String.valueOf(a.a(a.a())));
        assertEquals("[1, 2, 3, 4, 5, 6]", String.valueOf(a.a(6)));
        assertEquals("[3, 6, 4, 5, 1, 2]", String.valueOf(a.a(a.a()).a(a.a())));
    }
}
