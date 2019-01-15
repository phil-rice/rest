package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.names.MultipleInterfaceNames;

import java.util.ArrayList;
import java.util.List;
public class CompositeImplClassDom {
    private LoggerAdapter log;
    PackageAndClassName multipleInterfaceName;
    PackageAndClassName multipleImplName;
    PackageAndClassName rootImplName;

    public CompositeImplClassDom(LoggerAdapter log,  MultipleInterfaceNames multipleInterfaceNames) {
        this.log = log;
        this.multipleInterfaceName = multipleInterfaceNames.multipleInterfaceName;
        this.multipleImplName = multipleInterfaceNames.multipleInterfacesClientImplName;
        this.rootImplName = multipleInterfaceNames.entityNames.clientImplementation;
    }


    public List<String> createClass() {
        String packageName = multipleImplName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.add("import " + IXingYi.class.getName() + ";");
        result.add("import " + rootImplName.asString() + ";");

        result.add("public class " + multipleImplName.className + " extends " + rootImplName.className + " implements " + multipleInterfaceName.className + "{// classname[" + rootImplName.className + "]");
        result.addAll(Formating.indent(createConstructor()));
        result.add("}");
        return result;
    }

    List<String> createConstructor() {
        ArrayList<String> result = new ArrayList<>();
        result.add("public " + multipleImplName.className + "(Object mirror, IXingYi xingYi) {");
        result.add("   super(mirror, xingYi);");
        result.add("}");
        return result;

    }
}
