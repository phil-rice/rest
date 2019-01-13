package one.xingyi.restAnnotations.utils;
import one.xingyi.restAnnotations.http.ServiceResponse;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
public class OptionalUtils {
    public static Optional<String> fromString(String s) {
        if (s == null || s.length() == 0)
            return Optional.empty();
        else return Optional.of(s);
    }

    public static <T> CompletableFuture<Optional<T>> flip(Optional<CompletableFuture<T>> opt) {
        return opt.map(fut -> fut.thenApply(x -> Optional.of(x))).orElse(CompletableFuture.completedFuture(Optional.empty()));
    }

    public static <T> Optional<T> from(boolean b, Supplier<T> supplier) {
        return b ? Optional.of(supplier.get()) : Optional.empty();
    }
}
