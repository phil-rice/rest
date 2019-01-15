package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.clientside.IClientCompanion;
import one.xingyi.restAnnotations.clientside.IClientMaker;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.OptionalUtils;

import java.util.*;
import java.util.function.Function;
public class CompanionOnClientClassDom {
    private PackageAndClassName clientName;
    public final FieldList fields;
    private BookmarkAndUrlPattern bookmarkAndUrlPattern;
    private LoggerAdapter log;
    public INames names;
    public final PackageAndClassName companionName;

    public CompanionOnClientClassDom(LoggerAdapter log, INames names, EntityNames entityNames, FieldList fields, BookmarkAndUrlPattern bookmarkAndUrlPattern) {
        this.log = log;
        this.names = names;
        this.companionName = entityNames.clientCompanion;
        this.clientName = entityNames.clientImplementation;
        this.fields = fields;
        this.bookmarkAndUrlPattern = bookmarkAndUrlPattern;
    }


    public List<String> createClass() {
        String packageName = companionName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.add("import " + IClientCompanion.class.getName() + ";");
        result.add("import " + IClientMaker.class.getName() + ";");
        result.add("import " + Optional.class.getName() + ";");
        result.add("import " + IXingYi.class.getName() + ";");
        result.add("import " + Set.class.getName() + ";");
        result.add("import " + Function.class.getName() + ";");
        result.add("import " + OptionalUtils.class.getName() + ";");
        result.add("import " + clientName.asString() + ";");
        result.add("public class " + companionName.className + " implements IClientCompanion{");
        result.add(Formating.indent + "final static public " + companionName.className + " companion=new " + companionName.className + "();");
        result.addAll(Formating.indent(createSupported()));
        result.addAll(Formating.indent(createFindCompanion()));
        result.addAll(Formating.indent(createApply()));
        result.addAll(Formating.indent(createBookmark()));
        result.add("}");
        return result;
    }

    List<String> createBookmark() {
        return Arrays.asList("public String bookmark(){return \"" + bookmarkAndUrlPattern.bookmark + "\";} ");
    }
    List<String> createSupported() {
        return Arrays.asList("public Set<Class<?>> supported(){return Set.of(" + ListUtils.mapJoin(fields.nestedOps(), ",", s -> s + ".class") + ");} ");
    }
    List<String> createFindCompanion() {
        return Arrays.asList(
                "@Override public Function<Class<?>, Optional<IClientMaker>> findCompanion() {",
                Formating.indent + "return clazz -> OptionalUtils.from(supported().contains(clazz), () -> this);",
                "};");

    }

    List<String> createApply() {
        return Arrays.asList(
                "@Override public <Interface> Optional<Interface> apply(Class<Interface> clazz, IXingYi xingYi, Object mirror) {",
                Formating.indent + "if (supported().contains(clazz))",
                Formating.indent + Formating.indent + "return Optional.of((Interface)new " + clientName.className + "(mirror, xingYi));",
                Formating.indent + " return Optional.empty();",
                "}"
        );
    }
}
