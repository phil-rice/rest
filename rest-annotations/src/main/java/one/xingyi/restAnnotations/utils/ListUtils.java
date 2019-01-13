package one.xingyi.restAnnotations.utils;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
public class ListUtils {

    public static <T, T1> List<T1> map(List<T> list, Function<T, T1> fn) {
        List<T1> result = new ArrayList<>();
        for (T t : list)
            result.add(fn.apply(t));
        return result;
    }

    public static <Acc, V> Acc foldLeft(Acc zero, List<V> list, BiFunction<Acc, V, Acc> fn) {
        Acc acc = zero;
        for (V v : list) {
            acc = fn.apply(acc, v);
        }
        return acc;
    }

    public static <T> List<T> add(List<T> list, T t) {
        List<T> result = new ArrayList<>(list.size() + 1);
        result.addAll(list);
        result.add(t);
        return result;
    }
    public static <T> List<T> append(List<T>... lists) {
        List<T> result = new ArrayList<>();
        for (List<T> list : lists)
            result.addAll(list);
        return result;
    }
    public static <T> List<T> optionals(Optional<T>... optionals) {
        List<T> result = new ArrayList<>();
        for (Optional<T> list : optionals)
            list.ifPresent(result::add);
        return result;
    }
    public static <T, T1> List<T1> collect(List<T> list, Function<T, Boolean> filter, Function<T, T1> fn) {
        List<T1> result = new ArrayList<>();
        for (T t : list)
            if (filter.apply(t))
                result.add(fn.apply(t));
        return result;
    }
    public static <T, T1> List<T1> flatMap(List<T> list, Function<T, List<T1>> fn) {
        List<T1> result = new ArrayList<>();
        for (T t : list)
            for (T1 t1 : fn.apply(t))
                result.add(t1);
        return result;
    }
    public static <T> List<T> filter(List<T> list, Function<T, Boolean> fn) {
        List<T> result = new ArrayList<>();
        for (T t : list)
            if (fn.apply(t)) result.add(t);
        return result;
    }
    public static <T> List<T> unique(List<T> list) {
        List<T> result = new ArrayList<>();
        for (T t : list)
            if (!result.contains(t))
                result.add(t);
        return result;
    }
    public static <T> String join(List<T> list, String separator) {
        StringBuilder result = new StringBuilder();
        for (T t : list) {
            if (result.length() > 0)
                result.append(separator);
            result.append(t.toString());
        }
        return result.toString();

    }
    public static <T> String mapJoin(List<T> list, String separator, Function<T, String> fn) {
        StringBuilder result = new StringBuilder();
        for (T t : list) {
            if (result.length() > 0)
                result.append(separator);
            result.append(fn.apply(t));
        }
        return result.toString();
    }

}
