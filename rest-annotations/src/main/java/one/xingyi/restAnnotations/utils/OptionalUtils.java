package one.xingyi.restAnnotations.utils;
import one.xingyi.restAnnotations.http.ServiceResponse;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static one.xingyi.restAnnotations.javascript.IXingYiFactory.xingYi;
public class OptionalUtils {
    public static Optional<String> fromString(String s) {
        if (s == null || s.length() == 0)
            return Optional.empty();
        else return Optional.of(s);
    }

    public static <From, To> Function<From, Optional<To>> allReturnSameTo(List<Function<From, Optional<To>>> fns) {
        return from -> {
            Optional<To> saved = Optional.empty();
            for (Function<From, Optional<To>> fn : fns) {
                Optional<To> to = fn.apply(from);
                if (!to.isPresent()) return Optional.empty();
                if (((Optional) saved).isPresent() && to.get() != saved) return Optional.empty();
                saved = to;
            }
            return saved;
        };
    }

    public static <From, To> Function<From, Optional<To>> chainFn(List<Function<From, Optional<To>>> fns) {
        return from -> {
            for (Function<From, Optional<To>> fn : fns) {
                Optional<To> opt = fn.apply(from);
                if (opt.isPresent())
                    return opt;

            }
            return Optional.empty();
        };
    }

    public static <T> CompletableFuture<Optional<T>> flip(Optional<CompletableFuture<T>> opt) {
        return opt.map(fut -> fut.thenApply(x -> Optional.of(x))).orElse(CompletableFuture.completedFuture(Optional.empty()));
    }

    public static <T> Optional<T> from(boolean b, Supplier<T> supplier) {
        return b ? Optional.of(supplier.get()) : Optional.empty();
    }
}
