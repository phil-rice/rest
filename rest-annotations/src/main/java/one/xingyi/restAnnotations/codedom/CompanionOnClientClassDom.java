package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.clientside.IClientFactory;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.javascript.XingYiDomain;
import one.xingyi.restAnnotations.optics.Lens;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.lang.reflect.Array;
import java.util.*;
public class CompanionOnClientClassDom {
    private PackageAndClassName clientName;
    public final FieldList fields;
    private LoggerAdapter log;
    public INames names;
    public final PackageAndClassName companionName;

    public CompanionOnClientClassDom(LoggerAdapter log, INames names, PackageAndClassName companionName, PackageAndClassName clientName, FieldList fields) {
        this.log = log;
        this.names = names;
        this.companionName = companionName;
        this.clientName = clientName;
        this.fields = fields;
    }


    public List<String> createClass() {
        String packageName = companionName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.add("import " + IClientFactory.class.getName() + ";");
        result.add("import " + Optional.class.getName() + ";");
        result.add("import " + IXingYi.class.getName() + ";");
        result.add("import " + Set.class.getName() + ";");
        result.add("import " + clientName.asString() + ";");
        result.add("public class " + companionName.className + " implements IClientFactory{");
        result.addAll(Formating.indent(createSet()));
        result.addAll(Formating.indent(createMethod()));
        result.add("}");
        return result;
    }
    List<String> createSet() {
        return Arrays.asList("public Set<Class<?>> supported(){return Set.of(" + ListUtils.mapJoin(fields.nestedOps(), ",", s -> s + ".class") + ");} ");

    }

    List<String> createMethod() {
        return Arrays.asList(
                "@Override public <Interface> Optional<Interface> apply(Class<Interface> clazz, IXingYi xingYi, Object mirror) {",
                Formating.indent + "if (supported().contains(clazz))",
                Formating.indent + Formating.indent + "return Optional.of((Interface)new " + clientName.className + "(mirror, xingYi));",
                Formating.indent + " return Optional.empty();",
                "}"
        );
    }
}
