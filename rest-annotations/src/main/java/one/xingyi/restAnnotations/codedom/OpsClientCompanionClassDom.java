package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.annotations.XingYiGenerated;
import one.xingyi.restAnnotations.entity.Companion;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.entity.IOpsClientCompanion;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.names.OpsNames;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
public class OpsClientCompanionClassDom {

    public final PackageAndClassName companionName;
    private OpsNames opsNames;
    public final FieldList fields;
    public OpsClientCompanionClassDom(OpsNames opsNames, FieldList fields) {
        this.companionName = opsNames.opsClientCompanion;
        this.opsNames = opsNames;
        this.fields = fields;
    }

    public List<String> createClass() {
        String packageName = companionName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.addAll(fields.createImports());
        result.add("import " + IOpsClientCompanion.class.getName() + ";");
        result.add("import " + Embedded.class.getName() + ";");
        result.add("import " + List.class.getName() + ";");
        result.add("import " + Set.class.getName() + ";");
        result.add("import " + Arrays.class.getName() + ";");
        result.add("import " + Companion.class.getName() + ";");
        result.add("import " + IXingYi.class.getName() + ";");
        result.add("import " + XingYiGenerated.class.getName() + ";");
        result.add("import " + opsNames.entityNames.clientCompanion.asString() + ";");
        result.add("@XingYiGenerated");
        result.add("public class " + companionName.className + " implements IOpsClientCompanion<" + opsNames.entityNames.clientImplementation.className + ">{");
        result.add(Formating.indent + "final static public " + companionName.className + " companion=new " + companionName.className + "();");

        result.addAll(Formating.indent(createMainEntity()));
        result.addAll(Formating.indent(createLensNames()));
        result.addAll(Formating.indent(createMakeImplementation()));
//        result.addAll(Formating.indent(createReturnTypes()));
        result.add("}");
        return result;
    }

    List<String> createMainEntity() {
        return Arrays.asList("@XingYiGenerated", "public " + opsNames.entityNames.clientCompanion.className + " entityCompanion(){return " + opsNames.entityNames.clientCompanion.className + ".companion; }");
    }
    List<String> createLensNames() {
        List<String> lensnames = fields.mapWithDeprecated(fd -> "lens_" + fd.lensName );
        return Arrays.asList("@XingYiGenerated", "public Set<String> lensNames(){return Set.of(" + ListUtils.mapJoin(lensnames, ",", Strings::quote) + ");}//"+ fields);
    }
    List<String> createMakeImplementation() {
        return Arrays.asList("@XingYiGenerated  public " + opsNames.entityNames.clientImplementation.className + " makeImplementation(IXingYi xingYi, Object mirror) {",
                Formating.indent + "return new " + opsNames.entityNames.clientImplementation.className + "(mirror,xingYi);",
                "}");
    }
}
