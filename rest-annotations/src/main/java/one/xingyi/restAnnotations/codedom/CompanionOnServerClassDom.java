package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.entity.Companion;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
public class CompanionOnServerClassDom {
    public INames names;
    public final PackageAndClassName serverImpl;
    public final FieldList fields;
    private BookmarkAndUrlPattern bookmarkAndUrlPattern;
    public final PackageAndClassName interfaceName;
    public final PackageAndClassName companionName;

    public CompanionOnServerClassDom(INames names, EntityNames entityNames, FieldList fields, BookmarkAndUrlPattern bookmarkAndUrlPattern) {
        this.names = names;
        this.companionName = entityNames.serverCompanion;
        this.interfaceName = entityNames.entityInterface;
        this.serverImpl = entityNames.serverImplementation;
        this.fields = fields;
        this.bookmarkAndUrlPattern = bookmarkAndUrlPattern;
    }

    public List<String> createClass() {
        String packageName = companionName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.add("import " + Companion.class.getName() + ";");
        result.add("import " + Set.class.getName() + ";");
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
        return Arrays.asList("public String bookmark(){return \"" + bookmarkAndUrlPattern.bookmark + "\";} ");
    }
    List<String> createSupported() {
        return Arrays.asList("public Set<Class<?>> supported(){return Set.of(" + ListUtils.mapJoin(fields.nestedOps(), ",", s -> s + ".class") + ");} ");
    }


    List<String> makeJavascript() {
        List<String> result = new ArrayList<>();
        result.add("final String javascript = " + Strings.quote(""));
        result.addAll(Formating.indent(fields.map(fd -> "+ " + Strings.quote("function lens_" + fd.lensName + "(){ return lens('" + fd.name + "');};\\n"))));
        result.add(";");
        result.add("public String interfaceName() { return " + Strings.quote(interfaceName.className) + "; } ");
        result.add("public String entityName() { return " + Strings.quote(serverImpl.className) + "; } ");
        result.add("public String javascript() { return javascript; } ");
        return result;
    }


}
