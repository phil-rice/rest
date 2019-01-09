package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.ListUtils;
import one.xingyi.restAnnotations.Strings;

import javax.lang.model.element.Element;
import java.util.List;
import java.util.function.Function;
public class FieldList {

    final List<String> imports;
    final List<TypeName> fields;

    public static <T extends Element> FieldList create(List<T> elements) {
        return new FieldList(ListUtils.map(elements, TypeName::create));
    }

    public FieldList(List<TypeName> rawFields) {
        this.fields = ListUtils.map(rawFields, tn -> tn.mapType(t -> Strings.lastSegement("\\.", t)));
        this.imports = ListUtils.unique(ListUtils.map(rawFields, tn -> tn.type));
    }

    public <T> List<T> map(Function<TypeName, T> fn) {
        return ListUtils.map(fields, fn);
    }
    public <T> List<T> flatMap(Function<TypeName, List<T>> fn) {
        return ListUtils.flatMap(fields, fn);
    }
    public <T> String mapJoin(String separator, Function<TypeName, String> fn) {
        return ListUtils.<TypeName>mapJoin(fields, separator, fn);
    }

    public String createConstructorCall(String name) {
        return "new " + name + "(" + ListUtils.mapJoin(fields, ", ", nv -> nv.name) + ")";
    }

    public List<String> createImports() {
        return ListUtils.map(imports, i -> "import " + i + ";");
    }
    public List<String> createFields() {
        return ListUtils.map(fields, nv -> nv.type + " " + nv.name + ";");
    }

    @Override public String toString() {
        return "FieldList{" +
                "imports=" + imports +
                ", fields=" + fields +
                '}';
    }
}
