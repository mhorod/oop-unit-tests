import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(Enclosed.class)
public class JakubOskwarekFunctionsTest {
    public static class ConstantTest {
        // If some of these tests don't compile, please consider this:
        // Should *creating* a constant function ever throw an exception?

        @Test
        public void can_create_simple_constant_functions() throws GenericFunctionsException {
            Function<Integer, Integer> integerFunction = Functions.constant(42);
            assertEquals((Integer)42, integerFunction.compute(Collections.emptyList()));

            Function<String, String> stringFunction = Functions.constant("Sigma z falką");
            assertEquals("Sigma z falką", stringFunction.compute(Collections.emptyList()));
        }

        @Test
        public void constant_function_always_returns_the_value_it_was_constructed_with() throws GenericFunctionsException {
            Function<String, String> text = Functions.constant("Gucio z rootem");
            final int always = 1000; // adjust according to your notion of "always"
            for(int i = 0; i < always; i++)
                assertEquals("Gucio z rootem", text.compute(Collections.emptyList()));
        }

        @Test
        public void arity_of_a_constant_function_is_zero() {
            Function<String, String> text = Functions.constant("Szybka analiza");
            assertEquals(0, text.arity());
        }

        @Test
        public void passing_a_nonempty_list_of_arguments_to_a_constant_function_results_in_generic_functions_exception() {
            Function<String, String> text = Functions.constant("I'm constant and take no arguments");
            List<String> arguments = List.of("Bro", "u sure 'bout that?");
            assertThrows(GenericFunctionsException.class, () -> text.compute(arguments));
        }

        @Test
        public void null_is_a_valid_constant_value() throws GenericFunctionsException {
            Function<String, String> stringNothing = Functions.constant(null);
            assertNull(stringNothing.compute(Collections.emptyList()));

            Function<Integer, Integer> integerNothing = Functions.constant(null);
            assertNull(integerNothing.compute(Collections.emptyList()));
        }

        @Test
        public void declared_argument_type_can_be_anything() throws GenericFunctionsException {
            Function<Object, String> someFunction = Functions.constant("Yeah, whatever");
            //noinspection RedundantTypeArguments
            assertEquals("Yeah, whatever", someFunction.compute(Collections.<Object>emptyList()));
        }

        @Test
        public void provided_argument_type_can_be_a_subclass_of_the_declared_one() throws GenericFunctionsException {
            Function<Object, String> someFunction = Functions.constant("Even more whatever");
            assertEquals("Even more whatever", someFunction.compute(Collections.<String>emptyList()));
        }
    }

    public static class ProjTest {
        @Test
        public void can_create_simple_projection_functions() throws GenericFunctionsException {
            Function<Integer, Integer> integerFunction = Functions.proj(3, 1);
            assertEquals((Integer)42, integerFunction.compute(List.of(32, 42, 52)));

            Function<String, String> stringFunction = Functions.proj(7, 2);
            List<String> sevenArguments = List.of("According", "to", "all", "known", "laws", "of", "aviation");
            assertEquals("all", stringFunction.compute(sevenArguments));
        }

        @Test
        public void projection_always_returns_the_kth_of_its_arguments() throws GenericFunctionsException {
            Function<String, String> function = Functions.proj(3, 1);
            final int always = 1000; // adjust according to your notion of "always"
            for(int i = 0; i < always; i++)
                assertEquals("ma", function.compute(List.of("Ala", "ma", "kota")));
        }

        @Test
        public void arity_of_a_projection_is_n() throws GenericFunctionsException {
            Function<String, String> function = Functions.proj(42, 16);
            assertEquals(42, function.arity());
        }

        @Test
        public void passing_wrong_number_of_arguments_to_a_projection_results_in_generic_functions_exception()
            throws GenericFunctionsException
        {
            Function<String, String> function = Functions.proj(3, 1);
            assertThrows(GenericFunctionsException.class, () -> function.compute(List.of("A", "priori")));
            assertThrows(GenericFunctionsException.class, () -> function.compute(List.of("A", "priori", "and", "a", "posteriori")));
        }

        @Test
        public void cannot_construct_a_projection_with_nonpositive_number_of_arguments() {
            assertThrows(GenericFunctionsException.class, () -> Functions.proj(-1, 0));
            assertThrows(GenericFunctionsException.class, () -> Functions.proj(0, 0));
        }

        @Test
        public void cannot_construct_a_projection_with_invalid_index() {
            assertThrows(GenericFunctionsException.class, () -> Functions.proj(3, -1));
            assertThrows(GenericFunctionsException.class, () -> Functions.proj(3, 3));
            assertThrows(GenericFunctionsException.class, () -> Functions.proj(3, 5));
        }

        @Test
        public void nulls_are_valid_arguments_for_a_projection() throws GenericFunctionsException {
            Function<String, String> function = Functions.proj(3, 1);

            // Creating argument lists the long way because List.of doesn't like null arguments
            List<String> firstArguments = new ArrayList<>();
            firstArguments.add(null);
            firstArguments.add("I am a proud not-null");
            firstArguments.add(null);
            assertEquals("I am a proud not-null", function.compute(firstArguments));

            List<String> secondArguments = new ArrayList<>();
            secondArguments.add("I am not null");
            secondArguments.add(null);
            secondArguments.add("Neither am I");
            assertNull(null, function.compute(secondArguments));
        }

        @Test
        public void argument_type_of_a_projection_can_be_a_subclass_of_its_return_type()
            throws GenericFunctionsException
        {
            Function<String, Object> function = Functions.proj(3, 1);
            assertEquals("Two", function.compute(List.of("One", "Two", "Three")));
        }

        @Test
        public void provided_argument_type_can_be_a_subclass_of_the_declared_one() throws GenericFunctionsException {
            Function<Number, Object> someFunction = Functions.proj(3, 1);
            List<Integer> arguments = List.of(1, 2, 3);
            assertEquals(2, someFunction.compute(arguments));
        }
    }

    public static class ComposeTest {
        // If some of these tests don't compile, please consider loosening the type constraints in generics

        @Test
        public void can_create_simple_function_compositions() throws GenericFunctionsException {
            Function<String, Integer> getLength = new Function<>() {
                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Integer compute(List<? extends String> args) throws GenericFunctionsException {
                    if(args == null || args.size() != arity())
                        throw new GenericFunctionsException();
                    return args.get(0).length();
                }
            };

            Function<Integer, String> describe = new Function<>() {
                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public String compute(List<? extends Integer> args) throws GenericFunctionsException {
                    if(args == null || args.size() != arity())
                        throw new GenericFunctionsException();
                    return "Length is: " + args.get(0);
                }
            };

            Function<String, String> composition = Functions.compose(describe, List.of(getLength));
            assertEquals("Length is: 3", composition.compute(List.of("APL")));
        }

        @Test
        public void composing_with_a_constant_function_returns_a_constant_function() throws GenericFunctionsException {
            Function<Object, String> text = Functions.constant("I am the chosen one");
            Function<String, String> reverse = new StrRvs();
            Function<Object, String> composition = Functions.compose(reverse, List.of(text));
            final int always = 1000; // adjust according to your notion of "always"
            for(int i = 0; i < always; i++)
                assertEquals("eno nesohc eht ma I", composition.compute(Collections.emptyList()));
                assertEquals("eno nesohc eht ma I", composition.compute(Collections.<String>emptyList()));
        }

        @Test
        public void arity_of_composition_is_the_common_arity_of_the_inner_functions() throws GenericFunctionsException {
            Function<String, String> enumerate = new Function<>() {
                @Override
                public int arity() {
                    return 3;
                }

                @Override
                public String compute(List<? extends String> args) throws GenericFunctionsException {
                    if(args == null || args.size() != arity())
                        throw new GenericFunctionsException();
                    return "1." + args.get(0) + ",2." + args.get(1) + ",3." + args.get(2);
                }
            };
            Function<String, String> composition = Functions.compose(
                enumerate,
                List.of(new StrRvs(), new StrRvs(), new StrRvs())
            );
            assertEquals(1, composition.arity());
            assertEquals("1.pilf,2.pilf,3.pilf", composition.compute(List.of("flip")));
        }

        @Test
        public void composing_with_inner_functions_of_various_different_arities_results_in_generic_functions_exception()
            throws GenericFunctionsException
        {
            Function<String, String> outer = Functions.proj(2, 1);
            List<Function<String, String>> inner = List.of(
                Functions.constant("Never gonna give you up"),
                Functions.proj(3, 2)
            );
            assertThrows(GenericFunctionsException.class, () -> Functions.compose(outer, inner));
        }

        @Test
        public void outer_function_begin_null_results_in_generic_functions_exception() {
            List<Function<String, String>> inner = List.of(
                Functions.constant("Inner functions are ok"),
                Functions.constant("and so what?")
            );
            assertThrows(GenericFunctionsException.class, () -> Functions.compose(null, inner));
        }

        @Test
        public void list_of_inner_functions_being_null_results_in_generic_functions_exception()
            throws GenericFunctionsException
        {
            Function<String, String> outer = Functions.proj(2, 1);
            assertThrows(GenericFunctionsException.class, () -> Functions.compose(outer, null));
        }

        @Test
        public void one_of_the_inner_functions_being_null_results_in_generic_functions_exception()
            throws GenericFunctionsException
        {
            Function<String, String> outer = Functions.proj(2, 1);
            List<Function<String, String>> inner = new ArrayList<>(); // List.of doesn't like nulls;
            inner.add(Functions.constant("I am cool but down there is..."));
            inner.add(null);
            assertThrows(GenericFunctionsException.class, () -> Functions.compose(outer, inner));
        }

        @Test
        public void the_number_of_inner_functions_must_match_the_arity_of_the_outer_function()
            throws GenericFunctionsException
        {
            Function<String, String> outer = Functions.proj(2, 1);

            List<Function<String, String>> tooMany = List.of(
                Functions.constant("There are"),
                Functions.constant("too many"),
                Functions.constant("inner functions here")
            );
            assertThrows(GenericFunctionsException.class, () -> Functions.compose(outer, tooMany));

            List<Function<String, String>> tooFew = List.of(
                Functions.constant("Just one sad inner function here :(")
            );
            assertThrows(GenericFunctionsException.class, () -> Functions.compose(outer, tooFew));
            assertThrows(GenericFunctionsException.class, () -> Functions.compose(outer, Collections.emptyList()));
        }

        @Test
        public void the_arity_of_the_outer_function_can_be_zero()
            throws GenericFunctionsException
        {
            Function<String, String> outer = Functions.constant("I'm the one at the sail, I'm the master of my sea!");
            Function<String, String> composition = Functions.compose(outer, Collections.emptyList());
            assertEquals("I'm the one at the sail, I'm the master of my sea!", composition.compute(Collections.emptyList()));
        }
    }
}
