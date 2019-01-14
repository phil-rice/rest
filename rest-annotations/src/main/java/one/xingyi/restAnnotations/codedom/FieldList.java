package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.functions.FunctionWithError;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.Strings;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import java.util.List;
import java.util.function.Function;
public class FieldList {

    final List<String> imports;
    final List<FieldDetails> fields;
    final List<FieldDetails> nonDeprecatedfields;

    public static <T extends Element> FieldList create(LoggerAdapter log, INames names, String interfaceName, List<T> elements) {
        return new FieldList(log, ListUtils.map(elements, e -> FieldDetails.create(log, names, interfaceName, e)));
    }


    public FieldList(LoggerAdapter log, List<FieldDetails> fields) {
        this.fields = fields;
        this.imports = ListUtils.unique(ListUtils.map(fields, tn -> tn.type.fullNameOfEntity));
        this.nonDeprecatedfields = ListUtils.filter(fields, fd -> !fd.deprecated);
    }

    List<String> nestedOps() { return ListUtils.unique(flatMap(tn -> tn.allInterfaces())); }


    public <T> List<T> map(FunctionWithError<FieldDetails, T> fn) { return ListUtils.map(nonDeprecatedfields, fn); }
    //    public <T> List<T> mapincDeprecated(Function<FieldDetails, T> fn) { return ListUtils.map(fields, fn); }
    public <T> List<T> flatMap(Function<FieldDetails, List<T>> fn) { return ListUtils.flatMap(nonDeprecatedfields, fn); }
    //    public <T> List<T> flatMapincDeprecated(Function<FieldDetails, List<T>> fn) { return ListUtils.flatMap(fields, fn); }
    public <T> String mapJoin(String separator, Function<FieldDetails, String> fn) { return ListUtils.<FieldDetails>mapJoin(nonDeprecatedfields, separator, fn); }

    public String createConstructorCall(String name) {
        return "new " + name + "(" + mapJoin(", ", nv -> nv.name) + ")";
    }

    public List<String> createImports() {
        return ListUtils.map(imports, i -> "import " + Strings.extractFromOptionalEnvelope(Embedded.class.getName(), ">", i) + ";");
    }
//    public List<String> createFields() {
//        return ListUtils.map(fields, nv -> nv.type + " " + nv.name + ";");
//    }

    @Override public String toString() {
        return "FieldList{" +
                "imports=" + imports +
                ", fields=" + fields +
                '}';
    }
}
