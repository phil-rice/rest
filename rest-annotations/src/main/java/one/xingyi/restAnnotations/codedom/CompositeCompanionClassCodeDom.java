package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.annotations.XingYiCompositeInterface;
import one.xingyi.restAnnotations.annotations.XingYiGenerated;
import one.xingyi.restAnnotations.clientside.IClientCompanion;
import one.xingyi.restAnnotations.clientside.IClientMaker;
import one.xingyi.restAnnotations.clientside.IXingYiClientOps;
import one.xingyi.restAnnotations.entity.IOpsClientCompanion;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.names.MultipleInterfaceNames;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.OptionalUtils;
import one.xingyi.restAnnotations.utils.SetUtils;

import java.util.*;
import java.util.function.Function;
public class CompositeCompanionClassCodeDom {
    private final List<PackageAndClassName> parentClientInterfaceNames;
    private LoggerAdapter log;
    private INames names;
    private PackageAndClassName multipleInterfaceName;
    private PackageAndClassName multipleImplName;
    private final PackageAndClassName multipleCompanionName;
    private final PackageAndClassName rootCompanionName;


    public CompositeCompanionClassCodeDom(LoggerAdapter log, INames names, MultipleInterfaceNames interfaceNames, List<String> parentClientInterfaceNames) {
        this.log = log;
        this.names = names;

        this.multipleInterfaceName = interfaceNames.multipleInterfaceName;
        this.multipleImplName = interfaceNames.multipleInterfacesClientImplName;
        this.multipleCompanionName = interfaceNames.multipleInterfacesClientCompanion;
        this.rootCompanionName = interfaceNames.entityNames.clientCompanion;
//        this.rootImplName = interfaceNames.entityNames.clientImplementation;
        this.parentClientInterfaceNames = ListUtils.map(parentClientInterfaceNames, n -> new PackageAndClassName(n));
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
        result.add("import " + SetUtils.class.getName() + ";");
        result.add("import " + IClientMaker.class.getName() + ";");
        result.add("import " + IXingYiClientOps.class.getName() + ";");
        result.add("import " + IOpsClientCompanion.class.getName() + ";");
        result.add("import " + XingYiGenerated.class.getName() + ";");
        result.add("public class " + multipleCompanionName.className + " implements IClientCompanion, IOpsClientCompanion{");
        result.add(Formating.indent + "final static public " + multipleCompanionName.className + " companion=new " + multipleCompanionName.className + "();");
        result.addAll(Formating.indent(createSupported()));
        result.addAll(Formating.indent(createFindCompanion()));
        result.addAll(Formating.indent(createApply()));
        result.addAll(Formating.indent(createMakeImplementation()));
        result.addAll(Formating.indent(createBookmark()));
        result.addAll(Formating.indent(createEntityCompanion()));
        result.addAll(Formating.indent(createChildCompanions()));
        result.addAll(Formating.indent(createLens()));
        result.add("}");
        return result;
    }

    List<String> createBookmark() {
        return Arrays.asList("@XingYiGenerated","public String bookmark(){return " + rootCompanionName.asString() + ".companion.bookmark();} ");
    }
    List<String> createEntityCompanion() {
        return Arrays.asList("@XingYiGenerated","public IClientCompanion entityCompanion(){return " + rootCompanionName.asString() + ".companion;} ");
    }

    List<String> createSupported() {
        return Arrays.asList("@XingYiGenerated","public Set<Class<? extends IXingYiClientOps<?>>> supported(){return Set.of(" + multipleInterfaceName.asString() + ".class);} ");
    }
    List<String> createFindCompanion() {
        return Arrays.asList("@XingYiGenerated",
                "@Override public  Function<Class<? extends IXingYiClientOps<?>>, Optional<IOpsClientCompanion>> findCompanion() {",
                Formating.indent + "return clazz -> OptionalUtils.from(supported().contains(clazz), () -> this);",
                "};");

    }

    List<String> createApply() {
        return Arrays.asList(
                "@XingYiGenerated",
                "@SuppressWarnings(\"unchecked\")",
                "@Override public <Interface extends IXingYiClientOps<?>> Optional<Interface> apply(Class<Interface> clazz, IXingYi xingYi, Object mirror) {",
                Formating.indent + "if (supported().contains(clazz))",
                Formating.indent + Formating.indent + "return Optional.of((Interface)new " + multipleImplName.className + "(mirror, xingYi));",
                Formating.indent + " return Optional.empty();",
                "}"
        );
    }

    List<String> createChildCompanions() {
        String children = ListUtils.mapJoin(parentClientInterfaceNames, ",", i -> names.clientCompanionName(i).asString() + ".companion");
        return Arrays.asList(
                "//If you get a compilation here with funny spelled companions starting with a '.' it is probably because the ",
                "//"+ XingYiCompositeInterface.class.getSimpleName() + " is defined in the same project as the server objects",
                "//There is an awkward ordering problem, and to fix it a work around is to put the qualified class name rather than the simple",
                "//For example public interface IPersonNameAndAddress extends IXingYiMultipleOps<IPerson>,one.xingyi.restExample.IPersonAddress, one.xingyi.restExample.IPersonName {",
                "//Instead of public interface IPersonNameAndAddress extends IXingYiMultipleOps<IPerson>,IPersonName, IPersonAddress {\n",
                "@XingYiGenerated",
                "public List<IOpsClientCompanion> childCompanions(){",
                Formating.indent + "return Arrays.asList(" + children + ");",
                "}");
    }
    List<String> createLens() {
        String children = ListUtils.mapJoin(parentClientInterfaceNames, ",", i -> names.clientCompanionName(i).asString() + ".companion.lensNames()");
   return Arrays.asList("@XingYiGenerated","public Set<String> lensNames() {",
                Formating.indent + "return SetUtils.append(" + children + ");",
                "}");
    }
    List<String> createMakeImplementation() {
        return Arrays.asList("@XingYiGenerated  public " +multipleImplName.className+ " makeImplementation(IXingYi xingYi, Object mirror) {",
                Formating.indent + "return new " + multipleImplName.className + "(mirror,xingYi);",
                "}");
    }
}
