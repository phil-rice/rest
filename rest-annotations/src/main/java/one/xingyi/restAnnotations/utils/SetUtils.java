package one.xingyi.restAnnotations.utils;
import one.xingyi.restAnnotations.functions.FunctionWithError;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
public class SetUtils {


    public static <T> Set<T> append(Set<T>... lists) {
        Set<T> result = new HashSet<>();
        for (Set<T> list : lists)
            result.addAll(list);
        return result;
    }

}
