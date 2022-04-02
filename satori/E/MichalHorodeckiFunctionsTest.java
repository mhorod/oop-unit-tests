import org.junit.*;


import static org.junit.Assert.*;

import java.util.*;

public class MichalHorodeckiFunctionsTest
{
    static class InnerFunction implements Function<Number, Integer>
    {

        @Override
        public int arity()
        {
            return 1;
        }

        @Override
        public Integer compute(List<? extends Number> args)
                throws GenericFunctionsException
        {
            if (args == null || args.size() != arity())
                throw new GenericFunctionsException();
            return args.get(0).hashCode();
        }
    }

    static class OuterFunction implements Function<Object, Number>
    {

        @Override
        public int arity()
        {
            return 2;
        }

        @Override
        public Integer compute(List<?> args) throws GenericFunctionsException
        {
            if (args == null || args.size() != arity())
                throw new GenericFunctionsException();
            return args.get(0).hashCode();
        }
    }

    @Test
    public void composition_stress_test()
    {
        // Constructs input that aims to test all aspects of compose method at once
        // f: Object -> Number
        // g, h: Number -> Integer
        // result: Integer --(g, h)--> Number --(f)--> Object

        var f = new OuterFunction();
        var g = new InnerFunction();
        var h = new InnerFunction();
        List<InnerFunction> args = new ArrayList<>();
        args.add(g);
        args.add(h);
        try
        {
            Functions.<Integer, Number, Object>compose(f, args);
        }
        catch (Exception e)
        {
            fail("Provided functions should compose.");
        }
    }

    @Test
    public void compose_throws_exception_when_arguments_are_null()
    {
        var f = new OuterFunction();
        var g = new InnerFunction();
        var h = new InnerFunction();
        List<InnerFunction> args = new ArrayList<>();
        args.add(g);
        args.add(h);
        try
        {
            Functions.<Integer, Number, Object>compose(null, args);
            fail("Function should throw an exception when outer function is null");
        }
        catch (Exception e)
        {
            assertEquals(GenericFunctionsException.class, e.getClass());
        }

        try
        {
            Functions.<Integer, Number, Object>compose(f, null);
            fail("Function should throw an exception when list of inner functions is null");
        }
        catch (Exception e)
        {
            assertEquals(GenericFunctionsException.class, e.getClass());
        }
    }
}
