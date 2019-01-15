package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.clientside.IClientCompanion;
import one.xingyi.restAnnotations.clientside.IClientMaker;
import one.xingyi.restAnnotations.entity.IOpsClientCompanion;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.names.MultipleInterfaceNames;
import one.xingyi.restAnnotations.names.OpsNames;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.OptionalUtils;

import java.util.*;
import java.util.function.Function;
public class CompositeCompanionClassCodeDom {
    private final List<PackageAndClassName> parentInterfaceNames;
    private LoggerAdapter log;
    private INames names;
    private PackageAndClassName multipleInterfaceName;
    private PackageAndClassName multipleImplName;
    private final PackageAndClassName multipleCompanionName;
    private final PackageAndClassName rootCompanionName;


    public CompositeCompanionClassCodeDom(LoggerAdapter log, INames names, MultipleInterfaceNames interfaceNames, List<String> parentInterfaceNames) {
        this.log = log;
        this.names = names;

        this.multipleInterfaceName = interfaceNames.multipleInterfaceName;
        this.multipleImplName = interfaceNames.multipleInterfacesClientImplName;
        this.multipleCompanionName = interfaceNames.multipleInterfacesClientCompanion;
        this.rootCompanionName = interfaceNames.entityNames.clientCompanion;
//        this.rootImplName = interfaceNames.entityNames.clientImplementation;
        this.parentInterfaceNames = ListUtils.map(parentInterfaceNames, n -> new PackageAndClassName(n));
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
        result.add("import " + List.class.getName() + ";");
        result.add("import " + Arrays.class.getName() + ";");
        result.add("import " + ListUtils.class.getName() + ";");
        result.add("import " + IClientMaker.class.getName() + ";");
        result.add("import " + IOpsClientCompanion.class.getName() + ";");
        result.add("public class " + multipleCompanionName.className + " implements IClientCompanion, IOpsClientCompanion{");
        result.add(Formating.indent + "final static public " + multipleCompanionName.className + " companion=new " + multipleCompanionName.className + "();");
        result.addAll(Formating.indent(createSupported()));
        result.addAll(Formating.indent(createFindCompanion()));
        result.addAll(Formating.indent(createApply()));
        result.addAll(Formating.indent(createBookmark()));
        result.addAll(Formating.indent(createEntityCompanion()));
        result.addAll(Formating.indent(createChildCompanions()));
        result.addAll(Formating.indent(createLens()));
        result.add("}");
        return result;
    }

    List<String> createBookmark() {
        return Arrays.asList("public String bookmark(){return " + rootCompanionName.asString() + ".companion.bookmark();} ");
    }
    List<String> createEntityCompanion() {
        return Arrays.asList("public IClientCompanion entityCompanion(){return " + rootCompanionName.asString() + ".companion;} ");
    }

    List<String> createSupported() {
        return Arrays.asList("public Set<Class<?>> supported(){return Set.of(" + multipleInterfaceName.asString() + ".class);} ");
    }
    List<String> createFindCompanion() {
        return Arrays.asList(
                "@Override public Function<Class<?>, Optional<IClientCompanion>> findCompanion() {",
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

    List<String> createChildCompanions() {
        String children = ListUtils.mapJoin(parentInterfaceNames, ",", i -> names.clientCompanionName(i).asString() + ".companion");
        return Arrays.asList("public List<IOpsClientCompanion> childCompanions(){",
                Formating.indent + "return Arrays.asList(" + children + ");",
                "}");
    }
    List<String> createLens() {
        String children = ListUtils.mapJoin(parentInterfaceNames, ",", i -> names.clientCompanionName(i).asString() + ".companion.lensNames()");

//        String children = ListUtils.mapJoin(parentOps, ",", op -> op.opsClientCompanion.asString() + ".companion.lensNames()");
        return Arrays.asList("public List<String> lensNames() {",
                Formating.indent + "return ListUtils.append(" + children + ");",
                "}");
    }

    //public List<IOpsClientCompanion> childCompanions(){
    //        return Arrays.asList(Test11ClientCompanion.companion, Test12ClientCompanion.companion);
    //}
    //
    //    @Override public List<String> lensNames() {
    //        return ListUtils.unique(ListUtils.append(Test11ClientCompanion.companion.lensNames(), Test12ClientCompanion.companion.lensNames()));
    //    }
}
