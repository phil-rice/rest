package one.xingyi.restAnnotations.javascript;
import one.xingyi.restAnnotations.utils.Digestor;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;
public class JavascriptStoreTest {

    JavascriptFor javascriptFor() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("root", "jsRoot");
        map.put("one", "jsOne");
        map.put("two", "jsTwo");
        return JavascriptFor.map(map);
    }
    Digestor digestor = new Digestor() {
        @Override public String apply(String s) {
            return "digOf" + s;
        }
    };
    JavascriptStore store = JavascriptStore.finder(digestor, javascriptFor());
    JavascriptDetails rootDetails = new JavascriptDetails("root", "digOfjsRoot", "jsRoot");
    JavascriptDetails oneDetails = new JavascriptDetails("one", "digOfjsOne", "jsOne");
    JavascriptDetails twoDetails = new JavascriptDetails("two", "digOfjsTwo", "jsTwo");

    @Test
    public void testStoreKeepsOrderInsertsRootAtStartAndReturnsFromEmbededJavascriptFor() {
        assertEquals(Arrays.asList(rootDetails, oneDetails), store.find(Arrays.asList("one")));
        assertEquals(Arrays.asList(rootDetails, oneDetails), store.find(Arrays.asList("one")));
        assertEquals(Arrays.asList(rootDetails, oneDetails, twoDetails), store.find(Arrays.asList("one", "two")));
        assertEquals(Arrays.asList(rootDetails, twoDetails, oneDetails), store.find(Arrays.asList("two", "one")));
    }
    @Test
    public void testStoreReturnsAllDataIfNoLens() {
        assertEquals(Arrays.asList(rootDetails, oneDetails, twoDetails), store.find(Arrays.asList()));
    }
}