package one.xingyi.restAnnotations.utils;
import one.xingyi.restAnnotations.functions.FunctionWithError;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
public class SetUtils {


    public static <T> Set<T> appendKeepingOrder(Set<T>... lists) {
        Set<T> result = new LinkedHashSet<>();
        for (Set<T> list : lists)
            result.addAll(list);
        return result;
    }

    public static String sortedString(Set<String> names, String separator) {
        List<String> result = new ArrayList<>();
        result.addAll(names);
        result.sort((a,b)->a.compareTo(b));
        return ListUtils.join(result, separator);
    }
}
