import java.util.List;

public class StrRvs implements Function<String, String> {
    @Override
    public int arity() { return 1;     }
    @Override
    public String compute(List<? extends String> args) throws GenericFunctionsException {
        if (args.size() != arity())    throw new GenericFunctionsException();
        return new StringBuilder(args.get(0)).reverse().toString();
    }
}