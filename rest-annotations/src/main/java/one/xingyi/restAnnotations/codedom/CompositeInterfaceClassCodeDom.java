package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.clientside.IClientCompanion;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.utils.OptionalUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
public class CompositeInterfaceClassCodeDom {
    private LoggerAdapter log;
    private final INames names;
    private String multipleInterfaceName;
    private final PackageAndClassName implementationName;
    private final String clientName;
    private final List<String> interfaceNames;
    public CompositeInterfaceClassCodeDom(LoggerAdapter log, INames names, String multipleInterfaceName,PackageAndClassName implementationName, String clientName, List<String> interfaceNames) {
        this.log = log;
        this.names = names;
        this.multipleInterfaceName = multipleInterfaceName;
        this.implementationName = implementationName;
        this.clientName = clientName;
        this.interfaceNames = interfaceNames;
    }


    public List<String> createClass() {
        String packageName = implementationName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.add("import " + IClientCompanion.class.getName() + ";");
        result.add("import " + Optional.class.getName() + ";");
        result.add("import " + IXingYi.class.getName() + ";");
        result.add("import " + Set.class.getName() + ";");
        result.add("import " + Function.class.getName() + ";");
        result.add("import " + OptionalUtils.class.getName() + ";");

        result.add("public class " + implementationName.className + " extends " + clientName + " implements " + multipleInterfaceName + "{");
        result.addAll(Formating.indent(createConstructor()));
        result.add("}");
        return result;
    }

    List<String> createConstructor() {
        ArrayList<String> result = new ArrayList<>();
        result.add("public " + implementationName.className + "(Object mirror, IXingYi xingYi) {");
        result.add("   super(mirror, xingYi);");
        result.add("}");
        return result;

    }
}
