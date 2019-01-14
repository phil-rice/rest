package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.clientside.IClientCompanion;
import one.xingyi.restAnnotations.clientside.IClientMaker;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.utils.OptionalUtils;

import java.util.*;
import java.util.function.Function;
public class CompositeCompanionClassCodeDom {
    private LoggerAdapter log;
    private final INames names;
    private PackageAndClassName multipleInterfaceName;
    private PackageAndClassName multipleImplName;
    private final PackageAndClassName multipleCompanionName;
    private final PackageAndClassName rootCompanionName;
    private final PackageAndClassName rootImplName;

    public CompositeCompanionClassCodeDom(LoggerAdapter log, INames names, PackageAndClassName multipleInterfaceName, PackageAndClassName multipleImplName, PackageAndClassName multipleCompanionName, PackageAndClassName rootCompanionName, PackageAndClassName rootImplName) {
        this.log = log;
        this.names = names;
        this.multipleInterfaceName = multipleInterfaceName;
        this.multipleImplName = multipleImplName;
        this.multipleCompanionName = multipleCompanionName;
        this.rootCompanionName = rootCompanionName;
        this.rootImplName = rootImplName;
    }


    public List<String> createClass() {
        String packageName = multipleCompanionName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.add("import " + IClientCompanion.class.getName() + ";");
        result.add("import " + Set.class.getName() + ";");
        result.add("import " + Function.class.getName() + ";");
        result.add("import " + Optional.class.getName() + ";");
        result.add("import " + OptionalUtils.class.getName() + ";");
        result.add("import " + IXingYi.class.getName() + ";");
        result.add("import " + IClientMaker.class.getName() + ";");
        result.add("public class " + multipleCompanionName.className + " implements IClientCompanion{");
        result.add(Formating.indent + "final static public " + multipleCompanionName.className + " companion=new " + multipleCompanionName.className + "();");
        result.addAll(Formating.indent(createSupported()));
        result.addAll(Formating.indent(createFindCompanion()));
        result.addAll(Formating.indent(createApply()));
        result.addAll(Formating.indent(createBookmark()));
        result.add("}");
        return result;
    }

    List<String> createBookmark() {
        return Arrays.asList("public String bookmark(){return " + rootCompanionName.asString() + ".companion.bookmark();} ");
    }
    List<String> createSupported() {
        return Arrays.asList("public Set<Class<?>> supported(){return Set.of("+ multipleInterfaceName.asString() + ".class);} ");
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
                Formating.indent + Formating.indent + "return Optional.of((Interface)new " + multipleImplName.className + "(mirror, xingYi));",
                Formating.indent + " return Optional.empty();",
                "}"
        );
    }
}
