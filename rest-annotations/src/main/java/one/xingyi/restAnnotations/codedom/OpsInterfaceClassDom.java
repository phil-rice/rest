package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.clientside.IXingYiOps;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.names.OpsNames;
import one.xingyi.restAnnotations.optics.Lens;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public class OpsInterfaceClassDom {

    public final PackageAndClassName opsName;
    public final PackageAndClassName entityName;
    public final FieldList fields;

    public OpsInterfaceClassDom(OpsNames opsNames, FieldList fields) {
        this.opsName = opsNames.opsInterface;
        this.entityName = opsNames.entityNames.clientImplementation;
        this.fields = fields;
    }

    public List<String> createClass() {
        String packageName = opsName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.addAll(fields.createImports());
        result.add("import " + Lens.class.getName() + ";");
        result.add("import " + IXingYiOps.class.getName() + ";");
        result.add("import " + Embedded.class.getName() + ";");
        result.add("import " + entityName.asString() + ";");
        result.add("public interface " + opsName.className + " extends IXingYiOps<" + entityName.className+">{");
        result.addAll(Formating.indent(createFields()));
        result.add("}");
        return result;
    }

    private List<String> createFields() {
        return fields.flatMap(fd -> new LensDom(fields, opsName.className, fd.type.shortName,fd).createForInterfacesOnServer(opsName.className));
    }

}
