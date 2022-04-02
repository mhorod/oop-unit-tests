import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Arrays;

public class KacperTopolskiFunctionsTest {
    static class A1Class {}
    static class A2Class extends A1Class {}
    static class A3Class extends A2Class {}
    static class B1Class {}
    static class B2Class extends B1Class {}
    static class B3Class extends B2Class {}
    static class C1Class {}
    static class C2Class extends C1Class {}
    static class C3Class extends C2Class {}

    static class U {
        static <T, S> Function<T, S> n_ary_null(int n) {
            return new Function<>() {
                @Override
                public int arity() {
                    return n;
                }

                @Override
                public S compute(List<? extends T> args) {
                    return null;
                }
            };
        }
    }

    @Test
    public void constant_null_test() throws GenericFunctionsException {
        Function<Integer, Integer> f = Functions.constant(null);

        assertEquals(0, f.arity());
        assertNull(f.compute(Arrays.asList()));
    }
    void single_proj_out_of_bounds(int i, int j) {
        try {
            Functions.<String, String>proj(i, j);
            String m = "!! no exception for proj(" + i + ", " + j + ")";
            System.out.println(m);
            fail(m);
        } catch (GenericFunctionsException ignored) {}
    }
    @Test
    public void proj_out_of_bounds() {
        single_proj_out_of_bounds(-1, -1);
        single_proj_out_of_bounds(-1, 0);
        single_proj_out_of_bounds(0, 0);
        single_proj_out_of_bounds(2, -1);
        single_proj_out_of_bounds(2, 2);
        single_proj_out_of_bounds(2, 3);
    }
    @Test
    public void proj_derived_types() throws GenericFunctionsException {
        Functions.<A2Class, A1Class>proj(1, 0);
    }
    @Test
    public void compose_null_tests() throws GenericFunctionsException {
        try {
            Functions.<A1Class, B1Class, C1Class>compose(
                    null,
                    null
            );
            fail();
        } catch (GenericFunctionsException ignored) {}

        try {
            Functions.<A1Class, B1Class, C1Class>compose(
                    null,
                    Arrays.asList(
                            U.<A1Class, B1Class>n_ary_null(1)
                    )
            );
            fail();
        } catch (GenericFunctionsException ignored) {}

        try {
            Functions.<A1Class, B1Class, C1Class>compose(
                    U.<B1Class, C1Class>n_ary_null(1),
                    null
            );
            fail();
        } catch (GenericFunctionsException ignored) {}

        try {
            Functions.<A1Class, B1Class, C1Class>compose(
                    U.<B1Class, C1Class>n_ary_null(2),
                    Arrays.asList(
                            U.<A1Class, B1Class>n_ary_null(1),
                            null
                    )
            );
            fail();
        } catch (GenericFunctionsException ignored) {}

        Function<A1Class, C1Class> f = Functions.<A1Class, B1Class, C1Class>compose(
                U.<B1Class, C1Class>n_ary_null(1),
                Arrays.asList(
                        U.<A1Class, B1Class>n_ary_null(2)
                )
        );

        assertEquals(2, f.arity());

        assertNull(f.compute(
                Arrays.asList(
                        (A1Class) null,
                        (A1Class) null
                )
        ));
    }
    @Test
    public void compose_different_arity() {
        try {
            Functions.<A1Class, B1Class, C1Class>compose(
                    U.<B1Class, C1Class>n_ary_null(2),
                    Arrays.asList(
                            U.<A1Class, B1Class>n_ary_null(1),
                            U.<A1Class, B1Class>n_ary_null(2)
                    )
            );
            fail();
        } catch (GenericFunctionsException ignored) {}
    }
    @Test
    public void compose_with_inheritance() throws GenericFunctionsException {
        Function<A2Class, C2Class> f = Functions.<A2Class, B2Class, C2Class>compose(
                U.<B1Class, C3Class>n_ary_null(3),
                Arrays.asList(
                        U.<A2Class, B3Class>n_ary_null(2),
                        U.<A2Class, B2Class>n_ary_null(2),
                        U.<A1Class, B2Class>n_ary_null(2)
                )
        );

        assertEquals(2, f.arity());

        assertNull(f.compute(Arrays.asList(
                new A2Class(),
                new A3Class()
        )));
    }
}


