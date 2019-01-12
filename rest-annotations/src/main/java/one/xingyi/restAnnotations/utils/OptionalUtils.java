package one.xingyi.restAnnotations.utils;
import java.util.Optional;
public class OptionalUtils {
    public static Optional<String> fromString(String s) {
        if (s == null || s.length() == 0)
            return Optional.empty();
        else return Optional.of(s);
    }
}
