
import java.util.List;

public interface Function<T, S> {
	int arity();
	S compute(List<? extends T> args) throws GenericFunctionsException;
}