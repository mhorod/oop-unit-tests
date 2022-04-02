import java.util.List;

public class StrConcat implements Function<String, String> {
    @Override
    public int arity() { return 2; }
    @Override
    public String compute(List<? extends String> args) throws GenericFunctionsException {
        if (args.size() != arity())
            throw new GenericFunctionsException();
        return args.get(0).toString() + args.get(1);
    }
}