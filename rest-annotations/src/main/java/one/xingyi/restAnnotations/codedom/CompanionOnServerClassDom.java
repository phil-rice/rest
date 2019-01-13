package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.entity.Companion;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
public class CompanionOnServerClassDom {
    public INames names;
    public final PackageAndClassName entityName;
    public final FieldList fields;
    public final PackageAndClassName interfaceName;
    public final PackageAndClassName companionName;

    public CompanionOnServerClassDom(INames names, PackageAndClassName companionName, PackageAndClassName interfaceName, PackageAndClassName entityName, FieldList fields) {
        this.names = names;
        this.companionName = companionName;
        this.interfaceName = interfaceName;
        this.entityName = entityName;
        this.fields = fields;
    }

    public List<String> createClass() {
        String packageName = companionName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.add("import " + Companion.class.getName() + ";");
        result.add("import " + Set.class.getName() + ";");
        result.add("public class " + companionName.className + " implements Companion<" + interfaceName.className + ", " + entityName.className + ">{");
        result.add("");
        result.add(Formating.indent + "final static public " + companionName.className + " companion=new " + companionName.className + "();");
        result.add("");
        result.addAll(Formating.indent(makeJavascript()));
        result.add("");
        result.addAll(Formating.indent(createSupported()));
        result.add("}");
        return result;
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
        result.add("public String entityName() { return " + Strings.quote(entityName.className) + "; } ");
        result.add("public String javascript() { return javascript; } ");
        return result;
    }


}
