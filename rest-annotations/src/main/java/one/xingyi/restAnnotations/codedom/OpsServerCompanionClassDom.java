package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.entity.Companion;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.entity.IOpsServerCompanion;
import one.xingyi.restAnnotations.names.OpsNames;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.OptionalUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class OpsServerCompanionClassDom {

    public final PackageAndClassName companionName;
    private OpsNames opsNames;
    public final FieldList fields;
    public OpsServerCompanionClassDom(OpsNames opsNames, FieldList fields) {
        this.companionName = opsNames.opsServerCompanion;
        this.opsNames = opsNames;
        this.fields = fields;
    }

    public List<String> createClass() {
        String packageName = companionName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.addAll(fields.createImports());
        result.add("import " + IOpsServerCompanion.class.getName() + ";");
        result.add("import " + Embedded.class.getName() + ";");
        result.add("import " + List.class.getName() + ";");
        result.add("import " + Arrays.class.getName() + ";");
        result.add("import " + Companion.class.getName() + ";");
        result.add("import " + opsNames.entityNames.serverCompanion.asString() + ";");
        result.add("");
        result.add("//if you get a compilation error with mispelt items check if there was an earlier 'cannot find entity' warning in one of the inherited ops interfaces" );
        result.add("public class " + companionName.className + " implements IOpsServerCompanion{");
        result.addAll(Formating.indent(createMainEntity()));
        result.addAll(Formating.indent(createReturnTypes()));
        result.add("}");
        return result;
    }

    List<String> createMainEntity() {
        return Arrays.asList("public " + opsNames.entityNames.serverCompanion.className + " entityCompanion(){return " + opsNames.entityNames.serverCompanion.className + ".companion; }");
    }
    List<String> createReturnTypes() {
        List<String> returnTypes = fields.forInterfaceOnlyEntities(opsNames.opsInterface.className).map(fd -> fd.type.optEntityName.get().serverCompanion.asString() + ".companion");
        return Arrays.asList("public List<Companion<?,?>> returnTypes(){return Arrays.asList(" + ListUtils.join(returnTypes, ",") + ");}");

    }
}
