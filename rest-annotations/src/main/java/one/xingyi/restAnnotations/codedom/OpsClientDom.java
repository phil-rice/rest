package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.annotations.XingYiGenerated;
import one.xingyi.restAnnotations.clientside.IXingYiClientOps;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.names.OpsNames;

import java.util.ArrayList;
import java.util.List;
public class OpsClientDom {

    private LoggerAdapter log;
    public final OpsNames opsNames;
    public final FieldList fields;
    public OpsClientDom(LoggerAdapter log, OpsNames opsNames, FieldList fields) {
        this.log = log;
        this.opsNames = opsNames;
        this.fields = fields;
    }


    public List<String> createClass() {
        String packageName = opsNames.opsClientInterface.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
//        result.addAll(fields.createImports());
        result.add("import " + IXingYiClientOps.class.getName() + ";");
        result.add("import " + Embedded.class.getName() + ";");
        result.add("import " + XingYiGenerated.class.getName() + ";");
//        result.add("import " + Lens.class.getName() + ";");
//        result.add("import " + XingYiDomain.class.getName() + ";");
//        result.add("import " + Embedded.class.getName() + ";");
//        result.add("import " + packageName + "." + interfaceName.className + ";");
        result.add("");
        result.add("@XingYiGenerated");
//        result.add("//If you get a 'does not override error then:");
//        result.add("//   first rebuild every thing (for example 'mvn clean install'. Incremental compilation sometimes goes wrong)");
//        result.add("//   second check that the main interface actually declares every field in the inherited 'ops' interfaces. The field should be obvious from the error message");
//        String interfaceString = interfaceName.asString() + (interfaceNames.isEmpty() ? "" : "," + ListUtils.mapJoin(interfaceNames, ",", i -> i.serverName));
        result.add("public interface " + opsNames.opsClientInterface.className + " extends IXingYiClientOps<" + opsNames.entityNames.entityInterface.className + "> {");
        result.addAll(Formating.indent(createFields()));
//        result.addAll(Formating.indent(createConstructor()));
//        result.addAll(Formating.indent(createLens()));
        result.add("}");
        return result;
    }
    List<String> createFields() {
        return fields.flatMapWithDeprecated(fd -> new LensDom(fields, opsNames.opsClientInterface.className, fd.type.shortName, fd).createForClassOnClientInterface());

    }

}
