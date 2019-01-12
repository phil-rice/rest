package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.Strings;

import javax.lang.model.element.Element;
import java.util.List;
import java.util.function.Function;
public class FieldList {

    final List<String> imports;
    final List<FieldDetails> fields;
    final List<FieldDetails> nonDeprecatedfields;

    public static <T extends Element> FieldList create(List<T> elements) {
        return new FieldList(ListUtils.map(elements, FieldDetails::create));
    }

    public FieldList(List<FieldDetails> rawFields) {
        this.fields = ListUtils.map(rawFields, tn -> tn.mapType(t -> Strings.lastSegement("\\.", t)));
        this.imports = ListUtils.unique(ListUtils.map(rawFields, tn -> tn.type));
        this.nonDeprecatedfields = ListUtils.filter(fields, fd -> !fd.deprecated);
    }

    public <T> List<T> map(Function<FieldDetails, T> fn) { return ListUtils.map(nonDeprecatedfields, fn); }
    public <T> List<T> mapincDeprecated(Function<FieldDetails, T> fn) { return ListUtils.map(fields, fn); }
    public <T> List<T> flatMap(Function<FieldDetails, List<T>> fn) { return ListUtils.flatMap(nonDeprecatedfields, fn); }
    public <T> List<T> flatMapincDeprecated(Function<FieldDetails, List<T>> fn) { return ListUtils.flatMap(fields, fn); }
    public <T> String mapJoin(String separator, Function<FieldDetails, String> fn) { return ListUtils.<FieldDetails>mapJoin(nonDeprecatedfields, separator, fn); }

    public String createConstructorCall(String name) {
        return "new " + name + "(" + mapJoin(", ", nv -> nv.name) + ")";
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
