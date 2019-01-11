package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.util.List;
public interface Formating {
    String indent = "    ";
    static List<String> indent(List<String> list) {
        return ListUtils.map(list,s -> indent + s);
    }
}
