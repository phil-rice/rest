package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.annotations.ElementsAndOps;
import one.xingyi.restAnnotations.annotations.InterfaceData;
import one.xingyi.restAnnotations.annotations.XingYiGenerated;
import one.xingyi.restAnnotations.entity.Companion;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.OptionalUtils;
import one.xingyi.restAnnotations.utils.Strings;

import java.util.*;
public class ServerCompanionDom {
    private final List<InterfaceData> interfaces;
    private LoggerAdapter log;
    public INames names;
    public final PackageAndClassName serverImpl;
    public final FieldList fields;
    private BookmarkAndUrlPattern bookmarkAndUrlPattern;
    public final PackageAndClassName interfaceName;
    private ElementsAndOps elementsAndOps;
    public final PackageAndClassName companionName;

    public ServerCompanionDom(LoggerAdapter log, INames names, ElementsAndOps elementsAndOps, EntityNames entityNames, FieldList fields, BookmarkAndUrlPattern bookmarkAndUrlPattern) {
        this.log = log;
        this.names = names;
        this.elementsAndOps = elementsAndOps;
        this.companionName = entityNames.serverCompanion;
        this.interfaceName = entityNames.entityInterface;
        this.serverImpl = entityNames.serverImplementation;
        this.fields = fields;
        this.bookmarkAndUrlPattern = bookmarkAndUrlPattern;
        interfaces = OptionalUtils.fold(elementsAndOps.find(interfaceName.asString()), () -> Arrays.asList(), e -> e.interfaces);
    }

    public List<String> createClass() {
        String packageName = companionName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.add("import " + Companion.class.getName() + ";");
        result.add("import " + Set.class.getName() + ";");
        result.add("import " + XingYiGenerated.class.getName() + ";");
        result.add("@XingYiGenerated");
        result.add("public class " + companionName.className + " implements Companion<" + interfaceName.className + ", " + serverImpl.className + ">{");
        result.addAll(Formating.indent(createBookmark()));
        result.add("");
        result.add(Formating.indent + "final static public " + companionName.className + " companion=new " + companionName.className + "();");
        result.add("");
        result.addAll(Formating.indent(makeJavascript()));
        result.add("");
        result.addAll(Formating.indent(createSupported()));
        result.add("}");
        return result;
    }
    List<String> createBookmark() {
        return Arrays.asList("@XingYiGenerated","public String bookmark(){return \"" + bookmarkAndUrlPattern.bookmark + "\";} ");
    }
    List<String> createSupported() {
//        log.info("IN server/create supported for " + companionName + " interfaces" + interfaces);
        return Arrays.asList("@XingYiGenerated","public Set<Class<?>> supported(){return Set.of(" + ListUtils.mapJoin(interfaces, ",", s -> s.serverInterface.asString() + ".class") + ");} ");
    }
    List<String> createOpsCompanions() {
        return Arrays.asList("@XingYiGenerated","public Set<IOpsServerCompanion> opsCompanions(){return Set.of(" + ListUtils.mapJoin(interfaces, ",", s -> s.serverInterface.asString() + ".class") + ");} ");
    }


    List<String> makeJavascript() {
        List<String> result = new ArrayList<>();
        result.add("final String javascript = " + Strings.quote(""));
        result.addAll(Formating.indent(fields.map(fd -> "+ " + Strings.quote("function lens_" + fd.lensName + "(){ return lens('" + fd.name + "');};\\n"))));
        result.add(";");
        result.add("@XingYiGenerated public String interfaceName() { return " + Strings.quote(interfaceName.className) + "; } ");
        result.add("@XingYiGenerated public String entityName() { return " + Strings.quote(serverImpl.className) + "; } ");
        result.add("@XingYiGenerated public String javascript() { return javascript; } ");
        return result;
    }


}
