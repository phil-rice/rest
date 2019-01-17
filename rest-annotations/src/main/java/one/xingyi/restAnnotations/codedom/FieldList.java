package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.annotations.ElementsAndOps;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.functions.FunctionWithError;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.Strings;

import javax.lang.model.element.Element;
import java.util.List;
import java.util.function.Function;
public class FieldList {

    private static LoggerAdapter log;
    final List<String> imports;
    final List<FieldDetails> fields;
    final List<FieldDetails> nonDeprecatedfields;
     final List<FieldDetails> writableFields;

    public boolean isEmpty() {return fields.isEmpty();}

    public static <T extends Element> FieldList create(LoggerAdapter log, INames names, ElementsAndOps elementsAndOps, String interfaceName, List<T> elements) {
        FieldList.log = log;
        return new FieldList(log, ListUtils.map(elements, e -> FieldDetails.create(log, names, elementsAndOps, interfaceName, e)));
    }


    public FieldList(LoggerAdapter log, List<FieldDetails> fields) {
        this.fields = fields;
        this.imports = ListUtils.unique(ListUtils.map(fields, tn -> tn.type.fullNameOfEntity));
        this.nonDeprecatedfields = ListUtils.filter(fields, fd -> !fd.deprecated);
        this.writableFields = ListUtils.filter(fields, fd -> !fd.readOnly);
    }


    public FieldList forInterfaceOnlyEntities(String interfaceName) { return filter(fd -> fd.isPresent(interfaceName) && !fd.type.primitive);}

    public <T> List<T> map(FunctionWithError<FieldDetails, T> fn) { return ListUtils.map(nonDeprecatedfields, fn); }
    public <T> List<T> mapWithDeprecated(FunctionWithError<FieldDetails, T> fn) { return ListUtils.map(fields, fn); }
    public <T> List<T> mapWithWritable(FunctionWithError<FieldDetails, T> fn) { return ListUtils.map(writableFields, fn); }
    public <T> FieldList filter(Function<FieldDetails, Boolean> fn) { return new FieldList(log, ListUtils.filter(fields, fn)); }
    public <T> List<T> flatMap(Function<FieldDetails, List<T>> fn) { return ListUtils.flatMap(nonDeprecatedfields, fn); }
    public <T> List<T> flatMapWithDeprecated(Function<FieldDetails, List<T>> fn) { return ListUtils.flatMap(fields, fn); }
    public <T> String mapJoin(String separator, Function<FieldDetails, String> fn) { return ListUtils.<FieldDetails>mapJoin(nonDeprecatedfields, separator, fn); }
    public <T> String mapJoinWithDeprecated(String separator, Function<FieldDetails, String> fn) { return ListUtils.<FieldDetails>mapJoin(fields, separator, fn); }

    public String createConstructorCall(String name) {
        return "new " + name + "(" + mapJoin(", ", nv -> nv.name) + ")";
    }

    public List<String> createImports() {
        return ListUtils.map(imports, i -> "import " + Strings.extractFromOptionalEnvelope(Embedded.class.getName(), ">", i) + ";");
    }

    @Override public String toString() {
        return "FieldList{" +
                "imports=" + imports +
                ", fields=" + fields +
                '}';
    }
}
