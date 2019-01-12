package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.IXingYi;
import one.xingyi.restAnnotations.XingYiDomain;
import one.xingyi.restAnnotations.optics.Lens;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class EntityOnClientClassDom {
    public final FieldList fields;
    public final PackageAndClassName interfaceName;
    public INames names;
    public final PackageAndClassName packageAndClassName;

    public EntityOnClientClassDom(INames names, PackageAndClassName packageAndClassName, PackageAndClassName interfaceName, FieldList fields) {
        this.names = names;
        this.packageAndClassName = packageAndClassName;
        this.interfaceName = interfaceName;
        this.fields = fields;
    }

    EntityOnClientClassDom withFields(FieldList fields) {
        return new EntityOnClientClassDom(names, packageAndClassName, interfaceName, fields);
    }
    EntityOnClientClassDom withPackageAndClassName(PackageAndClassName packageAndClassName, PackageAndClassName interfaceName) {
        return new EntityOnClientClassDom(names, packageAndClassName, interfaceName, fields);
    }

    List<String> nestedOps() { return ListUtils.unique(fields.flatMap(tn -> tn.allInterfaces)); }
    public List<OpsInterfaceClassDom> nested() {
        return ListUtils.map(nestedOps(), opsName -> new OpsInterfaceClassDom(packageAndClassName.withName(opsName), interfaceName, fields));
    }

    List<String> interfaces() {
        return ListUtils.unique(ListUtils.add(fields.flatMap(fd -> fd.allInterfaces), interfaceName.className));
    }
    public List<String> createClass() {
        String packageName = packageAndClassName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.addAll(fields.createImports());
        result.add("import " + IXingYi.class.getName() + ";");
        result.add("import " + Lens.class.getName() + ";");
        result.add("import " + XingYiDomain.class.getName() + ";");
        result.add("import " + packageName + "." + interfaceName.className + ";");
        result.add("class " + packageAndClassName.className + " extends XingYiDomain implements " + ListUtils.join(interfaces(), ",") + "{");
        result.addAll(Formating.indent(createFields()));
        result.addAll(Formating.indent(createConstructor()));
        result.addAll(Formating.indent(createLensForServerClass()));
        result.add("}");
        return result;
    }


    public List<String> createFields() {
        return Arrays.asList("final IXingYi xingYi;");
    }

    public List<String> createLensForServerClass() {
        return fields.flatMap(tn -> new LensDom(fields, packageAndClassName.className, tn).createForClassOnClient(interfaceName));
    }

    public List<String> createConstructor() {
        return Arrays.asList("public " + packageAndClassName.className + "(IXingYi xingYi){ this.xingYi = xingYi;}");
    }
}
