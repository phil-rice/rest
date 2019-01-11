package one.xingyi.restAnnotations.optics;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Lens<A, B> {
    public static <A, B> Lens<A, B> create(Function<A, B> get, BiFunction<A, B, A> set) {return new Lens<>(get, set);}

    public Function<A, B> getFn;
    public BiFunction<A, B, A> setFn;

    public Lens(Function<A, B> get, BiFunction<A, B, A> set) {
        this.getFn = get;
        this.setFn = set;
    }

    public B get(A a) {
        return getFn.apply(a);
    }

    public A set(A a, B b) {
        return setFn.apply(a, b);
    }

    public <C> Lens<A, C> andThen(Lens<B, C> lens) {
        Function<A, C> newGet = getFn.andThen(lens.getFn);
        BiFunction<A, C, A> newSet = (a, c) -> setFn.apply(a, lens.setFn.apply(getFn.apply(a), c));
        return new Lens<A, C>(newGet, newSet);
    }

    public A transform(A a, Function<B, B> fn) {
        return setFn.apply(a, fn.apply(getFn.apply(a)));
    }


}
