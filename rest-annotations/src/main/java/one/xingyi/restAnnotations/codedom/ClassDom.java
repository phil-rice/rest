package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.optics.Lens;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
public class ClassDom {
    public final String className;
    public final FieldList fields;
    public final String packageName;
    public final String interfaceName;
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

    List<String> nestedInterfaces() { return ListUtils.unique(fields.flatMap(tn -> tn.allInterfaces)); }
    public List<ClassDom> nested() { return ListUtils.map(nestedInterfaces(), interfaceName -> new ClassDom(packageName, interfaceName, fields)); }
    public List<ClassDom> nestedAndSelf() { return ListUtils.add(nested(), this);}

    public List<String> createClass() {
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.addAll(fields.createImports());
        result.add("import " + Lens.class.getName() + ";");
        result.add("import " + packageName + "." + interfaceName + ";");
        result.add("public class " + className + " implements " + interfaceName + "{");
        result.addAll(createFields());
        result.addAll(createConstructor());
        result.addAll(createLensForServerClass());
        result.add("}");
        return result;
    }

    public List<String> createInterface() {
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.addAll(fields.createImports());
        result.add("import " + Lens.class.getName() + ";");
        result.add("public interface " + interfaceName + "{");
        result.addAll(createLensForInterface());
        result.add("}");
        return result;
    }

    public List<String> createFields() {
        return fields.map(nv -> nv.type + " " + nv.name + ";");
    }

    public List<String> createLensForServerClass() {
        return fields.flatMap(tn -> new LensDom(fields, className, tn).createForClassOnServer());
    }
    public List<String> createLensForInterface() {
        return fields.flatMap(tn -> new LensDom(fields, className, tn).createForInterfacesOnServer(interfaceName));
    }
    public List<String> createConstructor() {
        List<String> result = new ArrayList<>();
        result.add("public " + className + "(" + fields.mapJoin(",", nv -> nv.type + " " + nv.name) + "){");
        result.addAll(fields.map(nv -> "   this." + nv.name + "=" + nv.name + ";"));
        result.add("}");
        return result;
    }
}
