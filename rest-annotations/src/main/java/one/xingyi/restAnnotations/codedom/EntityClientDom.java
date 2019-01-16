package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.annotations.InterfaceData;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.javascript.XingYiDomain;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.optics.Lens;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class EntityClientDom {
    public final FieldList fields;
    private List<InterfaceData> interfaceNames;
    public final PackageAndClassName interfaceName;
    private LoggerAdapter log;
    public INames names;
    public final PackageAndClassName clientImpl;

    public EntityClientDom(LoggerAdapter log, INames names, EntityNames entityNames, FieldList fields, List<InterfaceData> interfaceNames) {
        this.log = log;
        this.names = names;
        this.clientImpl = entityNames.clientImplementation;
        this.interfaceName = entityNames.entityInterface;
        this.fields = fields;
        this.interfaceNames = interfaceNames;
    }


    public List<String> createClass() {
        String packageName = clientImpl.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.addAll(fields.createImports());
        result.add("import " + IXingYi.class.getName() + ";");
        result.add("import " + Lens.class.getName() + ";");
        result.add("import " + XingYiDomain.class.getName() + ";");
        result.add("import " + Embedded.class.getName() + ";");
        result.add("import " + packageName + "." + interfaceName.className + ";");
        result.add("");
        result.add("//If you get a 'does not override error then:");
        result.add("//   first rebuild every thing (for example 'mvn clean install'. Incremental compilation sometimes goes wrong)");
        result.add("//   second check that the main interface actually declares every field in the inherited 'ops' interfaces. The field should be obvious from the error message");
        String interfaceString = interfaceName.asString() + (interfaceNames.isEmpty() ? "" : "," + ListUtils.mapJoin(interfaceNames, ",", i -> i.name));
        result.add("public class " + clientImpl.className + " extends XingYiDomain implements " + interfaceString + "{");
        result.addAll(Formating.indent(createFields()));
        result.addAll(Formating.indent(createConstructor()));
        result.addAll(Formating.indent(createLens()));
        result.add("}");
        return result;
    }

    public List<String> createFields() {
        return Arrays.asList("final IXingYi xingYi;");
    }

    public List<String> createLens() {
        return fields.flatMapWithDeprecated(fd -> {
            return new LensDom(fields, clientImpl.className, fd.type.shortName, fd).createForClassOnClient();
        });
    }

    public List<String> createConstructor() {
        return Arrays.asList("public " + clientImpl.className + "(Object mirror, IXingYi xingYi){ super(mirror); this.xingYi = xingYi;}");
    }
}
