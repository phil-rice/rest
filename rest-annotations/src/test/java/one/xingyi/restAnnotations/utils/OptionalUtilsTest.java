package one.xingyi.restAnnotations.utils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.*;
import static one.xingyi.restAnnotations.utils.OptionalUtils.*;
public class OptionalUtilsTest {

    @Test
    public void testFromString() {
        assertEquals(Optional.empty(), fromString(null));
        assertEquals(Optional.empty(), fromString(""));
        assertEquals(Optional.of("abc"), fromString("abc"));
    }
    Function<Integer, Optional<String>> one = i -> from(i == 1, () -> "one");
    Function<Integer, Optional<String>> two = i -> from(i == 2, () -> "two");
    Function<Integer, Optional<String>> ch = chainFn(Arrays.asList(one, two));


    @Test
    public void testChainFn() {
        assertEquals(Optional.empty(), ch.apply(0));
        assertEquals(Optional.of("one"), ch.apply(1));
        assertEquals(Optional.of("two"), ch.apply(2));
        assertEquals(Optional.empty(), ch.apply(3));

    }

}