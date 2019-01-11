package one.xingyi.restAnnotations.optics;
import java.util.function.Function;
public interface Getter<A, B> {
    B get(A a);
    default <C> Getter<A, C> andThenGet(Getter<B, C> other) {
        return a -> other.get(get(a));
    }

}
