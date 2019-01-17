package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.annotations.ElementsAndOps;
import one.xingyi.restAnnotations.annotations.InterfaceData;
import one.xingyi.restAnnotations.annotations.XingYiGenerated;
import one.xingyi.restAnnotations.clientside.IClientCompanion;
import one.xingyi.restAnnotations.clientside.IClientMaker;
import one.xingyi.restAnnotations.clientside.IXingYiClientOps;
import one.xingyi.restAnnotations.entity.IOpsClientCompanion;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.OptionalUtils;

import java.util.*;
import java.util.function.Function;
public class EntityClientCompanionDom {
    private final List<InterfaceData> interfaces;
    private PackageAndClassName clientName;
    public final FieldList fields;
    private BookmarkAndUrlPattern bookmarkAndUrlPattern;
    private LoggerAdapter log;
    public INames names;
    public final PackageAndClassName companionName;

    public EntityClientCompanionDom(LoggerAdapter log, INames names, ElementsAndOps elementsAndOps, EntityNames entityNames, FieldList fields, BookmarkAndUrlPattern bookmarkAndUrlPattern) {
        this.log = log;
        this.names = names;
        this.companionName = entityNames.clientCompanion;
        this.clientName = entityNames.clientImplementation;
        this.fields = fields;
        this.bookmarkAndUrlPattern = bookmarkAndUrlPattern;
        interfaces = elementsAndOps.findInterfaces(entityNames.entityInterface.asString());


    }


    public List<String> createClass() {
        String packageName = companionName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.add("import " + IClientCompanion.class.getName() + ";");
        result.add("import " + IOpsClientCompanion.class.getName() + ";");
        result.add("import " + IClientMaker.class.getName() + ";");
        result.add("import " + Optional.class.getName() + ";");
        result.add("import " + IXingYi.class.getName() + ";");
        result.add("import " + Set.class.getName() + ";");
        result.add("import " + Map.class.getName() + ";");
        result.add("import " + Function.class.getName() + ";");
        result.add("import " + OptionalUtils.class.getName() + ";");
        result.add("import " + IXingYiClientOps.class.getName() + ";");
        result.add("import " + XingYiGenerated.class.getName() + ";");
        result.add("import " + clientName.asString() + ";");
        result.add("@XingYiGenerated");
        result.add("//" + interfaces);
        result.add("public class " + companionName.className + " implements IClientCompanion{");
        result.add(Formating.indent + "final static public " + companionName.className + " companion=new " + companionName.className + "();");
        result.addAll(Formating.indent(createSupported()));
        result.addAll(Formating.indent(createClientOpsCompanionMap()));
        result.addAll(Formating.indent(createFindCompanion()));
        result.addAll(Formating.indent(createApply()));
        result.addAll(Formating.indent(createBookmark()));
        result.add("}");
        return result;
    }

    List<String> createBookmark() {
        return Arrays.asList("@XingYiGenerated", "public String bookmark(){return \"" + bookmarkAndUrlPattern.bookmark + "\";} ");
    }
    List<String> createSupported() {
        return Arrays.asList("@XingYiGenerated", "public Set<Class<? extends IXingYiClientOps<?>>> supported(){return Set.of(" + ListUtils.mapJoin(interfaces, ",", s -> s.clientInterface.asString() + ".class") + ");} ");
    }
    List<String> createClientOpsCompanionMap() {
        return Arrays.asList("@XingYiGenerated",
                "public Map<Class<? extends IXingYiClientOps<?>>, IOpsClientCompanion> theMap = Map.of(",
                Formating.indent + ListUtils.mapJoin(interfaces, ",\n" + Formating.indent + Formating.indent,
                        id -> id.clientInterface.asString() + ".class," + names.clientCompanionName(id.clientInterface).asString() + ".companion") + ");");
//                IEntityInterfaces.class, EntityInterfacesClientCompanion.companion,
//                IEntityUrlPattern.class, EntityUrlPatternClientCompanion.companion);

    }
    List<String> createFindCompanion() {
        return Arrays.asList(
                "@XingYiGenerated",
                "@Override public Function<Class<? extends IXingYiClientOps<?>>, Optional<IOpsClientCompanion>> findCompanion() {",
                Formating.indent + "return clazz -> Optional.ofNullable(theMap.get(clazz));",
                "};");

    }

    List<String> createApply() {
        return Arrays.asList(
                "@XingYiGenerated",
                "@SuppressWarnings(\"unchecked\")",
                "@Override public <Interface extends IXingYiClientOps<?>> Optional<Interface> apply(Class<Interface> clazz, IXingYi xingYi, Object mirror) {",
                Formating.indent + "if (supported().contains(clazz))",
                Formating.indent + Formating.indent + "return Optional.of((Interface)new " + clientName.className + "(mirror, xingYi));",
                Formating.indent + " return Optional.empty();",
                "}"
        );
    }
}
