package one.xingyi.restAnnotations.utils;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
public class OptionalUtils {
    public static Optional<String> fromString(String s) {
        if (s == null || s.length() == 0)
            return Optional.empty();
        else return Optional.of(s);
    }

    public static <T> Optional<T> from(boolean b, Supplier<T> supplier){
        return b ? Optional.of(supplier.get()) : Optional.empty();
    }
}
