package one.xingyi.restAnnotations.entity;
import java.util.Map;
import java.util.Set;
public interface Companion<Interface,Entity> {
    String bookmark();
    String interfaceName();
    Set<Class<?>> supported();
    String entityName();
    String javascript();
    Map<String,String> javascriptMap();
}
