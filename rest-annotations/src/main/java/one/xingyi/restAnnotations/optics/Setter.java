package one.xingyi.restAnnotations.optics;
import java.util.function.BiFunction;
public interface Setter<A, B> {
    A set(A a, B b);
}
