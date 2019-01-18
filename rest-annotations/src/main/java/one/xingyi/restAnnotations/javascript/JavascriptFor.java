package one.xingyi.restAnnotations.javascript;
import java.util.Map;
import java.util.Set;
public interface JavascriptFor {
    String javascriptFor(String lens);
    Set<String> allLens();

    static JavascriptFor map(Map<String, String> map) {
        return new JavascriptFor() {
            @Override public String javascriptFor(String lens) {
                return map.get(lens);
            }
            @Override public Set<String> allLens() {
                return map.keySet();
            }
        };
    }
}
