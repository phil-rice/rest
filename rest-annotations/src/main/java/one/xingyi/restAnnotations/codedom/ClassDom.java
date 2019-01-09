package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.lens.Lens;

import java.util.ArrayList;
import java.util.List;
public class ClassDom {
    public final String className;
    final FieldList fields;
    private String packageName;
    private String interfaceName;
    public String implName(String interfaceName) {
        if (interfaceName.startsWith("I")) return interfaceName.substring(1);
        else
            return interfaceName + "Impl";
    }

    public ClassDom(String packageName, String interfaceName, FieldList fields) {
        this.packageName = packageName;
        this.interfaceName = interfaceName;
        this.className = implName(interfaceName);
        this.fields = fields;
    }

    public List<String> createClass() {
        ArrayList<String> result = new ArrayList<>();
        result.add("package "+ packageName + ";");
        result.addAll(fields.createImports());
        result.add("import " + Lens.class.getName() + ";");
        result.add("import " + packageName + "." + interfaceName + ";");
        result.add("public class " + className + " implements " + interfaceName + "{");
        result.addAll(createFields());
        result.addAll(createConstructor());
        result.addAll(createLens());
        result.add("}");
        return result;
    }
    public List<String> createFields() {
        return fields.map(nv -> nv.type + " " + nv.name + ";");
    }

    public List<String> createLens() {
        return fields.flatMap(tn -> new LensDom(fields, className, tn.type, tn.name).build());
    }
    public List<String> createConstructor() {
        List<String> result = new ArrayList<>();
        result.add("public " + className + "(" + fields.mapJoin(",", nv -> nv.type + " " + nv.name) + "){");
        result.addAll(fields.map(nv -> "   this." + nv.name + "=" + nv.name + ";"));
        result.add("}");
        return result;
    }
}
