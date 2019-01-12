package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.IXingYi;
import one.xingyi.restAnnotations.optics.Lens;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
public class EntityOnServerClassDom {
    public final FieldList fields;
    public final PackageAndClassName interfaceName;
    public INames names;
    public final PackageAndClassName packageAndClassName;

    public EntityOnServerClassDom(INames names, PackageAndClassName packageAndClassName, PackageAndClassName interfaceName, FieldList fields) {
        this.names = names;
        this.packageAndClassName = packageAndClassName;
        this.interfaceName = interfaceName;
        this.fields = fields;
    }

    EntityOnServerClassDom withFields(FieldList fields) {
        return new EntityOnServerClassDom(names, packageAndClassName, interfaceName, fields);
    }
    EntityOnServerClassDom withPackageAndClassName(PackageAndClassName packageAndClassName, PackageAndClassName interfaceName) {
        return new EntityOnServerClassDom(names, packageAndClassName, interfaceName, fields);
    }

    List<String> nestedOps() { return ListUtils.unique(fields.flatMap(tn -> tn.allInterfaces)); }
    public List<OpsInterfaceClassDom> nested() {
        return ListUtils.map(nestedOps(), opsName -> new OpsInterfaceClassDom(packageAndClassName.withName(opsName), interfaceName, fields));
    }

    public List<String> createClass() {
        String packageName = packageAndClassName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.addAll(fields.createImports());
        result.add("import " + Lens.class.getName() + ";");
        result.add("import " + Objects.class.getName() + ";");
        result.add("import " + packageName + "." + interfaceName.className + ";");
        result.add("public class " + packageAndClassName.className + " implements " + interfaceName.className + "{");
        result.addAll(Formating.indent(createFields()));
        result.addAll(Formating.indent(createConstructor()));
        result.addAll(Formating.indent(createLensForServerClass()));
        result.addAll(Formating.indent(createEquals()));
        result.addAll(Formating.indent(createHashcode()));
        result.add("}");
        return result;
    }

    public List<String> createFields() {
        return fields.map(nv -> "final " + nv.type + " " + nv.name + ";");
    }

    public List<String> createLensForServerClass() {
        return fields.flatMap(tn -> new LensDom(fields, packageAndClassName.className, tn).createForClassOnServer());
    }


    public List<String> createConstructor() {
        List<String> result = new ArrayList<>();
        result.add("public " + packageAndClassName.className + "(" + fields.mapJoin(",", nv -> nv.type + " " + nv.name) + "){");
        result.addAll(fields.map(nv -> Formating.indent + "this." + nv.name + "=" + nv.name + ";"));
        result.add("}");
        return result;
    }

    public List<String> createEquals() {
        List<String> result = new ArrayList<>();
        result.add("@Override public boolean equals(Object o) {");
        result.add(Formating.indent + "if (this == o) return true;");
        result.add(Formating.indent + "if (o == null || getClass() != o.getClass()) return false;");
        result.add(Formating.indent + packageAndClassName.className + " other = (" + packageAndClassName.className + ") o;");
        result.add(Formating.indent+ "return "+ fields.mapJoin(" && ", fd ->  "Objects.equals(" + fd.name + ",other." + fd.name + ")") +";");
        result.add("}");
        return result;
    }
    public List<String> createHashcode() {
        List<String> result = new ArrayList<>();
        result.add("@Override public int hashCode() {");
        result.add(Formating.indent + "return Objects.hash(" + fields.mapJoin(",", fd -> fd.name) +");");
        result.add("}");
        return result;

    }

//    @Override public int hashCode() {
//        return Objects.hash(name, telephone, address);
//    }


}
