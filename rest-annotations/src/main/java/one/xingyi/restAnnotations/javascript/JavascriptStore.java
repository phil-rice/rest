package one.xingyi.restAnnotations.javascript;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.entity.EntityRegister;
import one.xingyi.restAnnotations.utils.Digestor;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.SetUtils;

import java.util.*;
public interface JavascriptStore {
    List<JavascriptDetails> find(List<String> lensNames);

    static JavascriptStore finder(Digestor digestor, JavascriptFor javascriptFor) {return new SimpleJavascriptStore(digestor, javascriptFor);}
    static String javascript(JavascriptStore store, List<String> lensNames) {
        return ListUtils.mapJoin(store.find(lensNames), "\n", jd -> jd.javascriptFragment);
    }

}


@EqualsAndHashCode
@ToString
class SimpleJavascriptStore implements JavascriptStore {
    public SimpleJavascriptStore(Digestor digestor, JavascriptFor javascriptFor) {
        this.map = new LinkedHashMap<>();
        this.legalNames = SetUtils.sortedString(javascriptFor.allLens(), ", ");
        for (String lens : javascriptFor.allLens()) {
            String javascript = javascriptFor.javascriptFor(lens);
            String digest = digestor.apply(javascript);
            map.put(lens, new JavascriptDetails(lens, digest, javascript));
        }
    }
    final String legalNames;
    final Map<String, JavascriptDetails> map;

    @Override public List<JavascriptDetails> find(List<String> lensNames) {
        if (lensNames.size() == 0)
            return all();
        else
            return ListUtils.map(ListUtils.insert("root", lensNames), name -> Optional.ofNullable(map.get(name)).orElseThrow(() -> notFound(name)));
    }
    List<JavascriptDetails> all() {
        List<String> keysWithoutRoot = new ArrayList<>();
        keysWithoutRoot.addAll(map.keySet());
        keysWithoutRoot.remove("root");
        return ListUtils.map(ListUtils.insert("root", keysWithoutRoot), map::get);
    }
    private RuntimeException notFound(String name) {
        return new RuntimeException("Cannot find javascript for lens " + name + " legal values are " + legalNames);
    }
}
