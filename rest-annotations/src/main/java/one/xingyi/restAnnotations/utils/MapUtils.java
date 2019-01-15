package one.xingyi.restAnnotations.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class MapUtils {
    public static <K,V> void add(Map<K, List<V>> map, K k, V v){
        List<V> list = map.get(k);
        if (list == null){
            list = new ArrayList<>();
            map.put(k,list);
        }
        list.add(v);
    }

}
