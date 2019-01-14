package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.javascript.IXingYi;

import java.util.ArrayList;
import java.util.List;
public class CompositeImplClassDom {
    private LoggerAdapter log;
    private final INames names;
    PackageAndClassName multipleInterfaceName;
    PackageAndClassName multipleImplName;
    PackageAndClassName rootImplName;
    private final List<String> interfaceNames;
    public CompositeImplClassDom(LoggerAdapter log, INames names, PackageAndClassName multipleInterfaceName, PackageAndClassName multipleImplName, PackageAndClassName rootImplName, List<String> interfaceNames) {
        this.log = log;
        this.names = names;
        this.multipleInterfaceName = multipleInterfaceName;
        this.multipleImplName = multipleImplName;
        this.rootImplName = rootImplName;
        this.interfaceNames = interfaceNames;
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
