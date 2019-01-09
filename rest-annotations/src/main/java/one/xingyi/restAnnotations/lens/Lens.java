package one.xingyi.restAnnotations.lens;
import java.util.function.BiFunction;
import java.util.function.Function;
public class Lens<T1, T2> {
    public static <T1, T2> Lens<T1, T2> create(Function<T1, T2> get, BiFunction<T1, T2, T1> set) {
        return null;
    }
    public <T3> Lens<T1, T3> andThen(Lens<T2, T3> other) {
        return null;
    }

}
