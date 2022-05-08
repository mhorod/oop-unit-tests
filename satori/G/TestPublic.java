import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TestPublic {
    @Test
    public void test() {

        List<Integer> s1 = StreamUtils.generateRest(Stream.of(1, 1),
                (a, b)-> a+b).limit(7).collect( Collectors.toList() );
        assertEquals("[1, 1, 2, 3, 5, 8, 13]", s1.toString() );

        ArrayList<String> s2 = new ArrayList();
        StreamUtils.generateRest(Stream.of("Ala", "ma", "kota"),
                (a, b)-> a+b).limit(7).forEach(s2::add);
        assertEquals("[Ala, ma, kota, makota, kotamakota, makotakotamakota, kotamakotamakotakotamakota]", s2.toString() );

        ArrayList<Integer> s3 = new ArrayList();
        StreamUtils.generateRest(Stream.of(i -> 0),
                (BinaryOperator<UnaryOperator<Integer>>) (f, g) ->
                (x -> x==0? 1: x*g.apply(x-1))).
                limit(10).map(f->f.apply(7)).forEach(s3::add);
        assertEquals("[0, 0, 0, 0, 0, 0, 0, 0, 5040, 5040]", s3.toString() );

    }

}
