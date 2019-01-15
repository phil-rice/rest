package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.clientside.IXingYiOps;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.entity.IOpsCompanion;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.OpsNames;
import one.xingyi.restAnnotations.optics.Lens;
import one.xingyi.restAnnotations.utils.OptionalUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
public class OpsCompanionClassDom {

    public final PackageAndClassName companionName;
    private OpsNames opsNames;
    public final FieldList fields;
    public OpsCompanionClassDom(OpsNames opsNames, FieldList fields) {
        this.companionName = opsNames.opsServerCompanion;
        this.opsNames = opsNames;
        this.fields = fields;
    }

    public List<String> createClass() {
        String packageName = companionName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.addAll(fields.createImports());
        result.add("import " + IOpsCompanion.class.getName() + ";");
        result.add("import " + Embedded.class.getName() + ";");
        result.add("import " + opsNames.entityNames.serverCompanion.asString() + ";");
        result.add("public class " + companionName.className + " implements IOpsCompanion{");
        result.addAll(Formating.indent(createMainEntity()));
        result.add("}");
        return result;
    }

    List<String> createMainEntity() {
        return Arrays.asList("public " + opsNames.entityNames.serverCompanion.className + " entityCompanion(){return " + opsNames.entityNames.serverCompanion.className + ".companion; }");
    }
    List<String> createReturnTypes() {
        return fields.flatMap(fd -> OptionalUtils.fold(fd.type.optEntityName, () -> Arrays.asList(), en -> Arrays.asList(en.clientCompanion.asString())));

    }
}
