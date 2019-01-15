package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.entity.EmbeddedWithHasJson;
import one.xingyi.restAnnotations.marshelling.ContextForJson;
import one.xingyi.restAnnotations.marshelling.HasJson;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.optics.Lens;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.Strings;

import java.util.*;
public class EntityOnServerClassDom {
    private LoggerAdapter log;
    public final FieldList fields;
    public final PackageAndClassName interfaceName;
    public INames names;
    public final PackageAndClassName packageAndClassName;

    public EntityOnServerClassDom(LoggerAdapter log, INames names, EntityNames entityNames, FieldList fields) {
        this.log = log;
        this.names = names;
        this.packageAndClassName = entityNames.serverImplementation;
        this.interfaceName = entityNames.entityInterface;
        this.fields = fields;
//        log.info("The fields in 'enityOnServerDom' for " + packageAndClassName + "are " + fields);
    }


    public List<OpsInterfaceClassDom> nested() {
        return ListUtils.map(fields.nestedOps(), opsName -> new OpsInterfaceClassDom(packageAndClassName.withName(opsName), interfaceName, fields));
    }

    public List<String> createClass() {
        String packageName = packageAndClassName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.addAll(fields.createImports());
        result.add("import " + EmbeddedWithHasJson.class.getName() + ";");
        result.add("import " + Lens.class.getName() + ";");
        result.add("import " + Strings.class.getName() + ";");
        result.add("import " + Objects.class.getName() + ";");
        result.add("import " + HasJson.class.getName() + ";");
        result.add("import " + JsonTC.class.getName() + ";");
        result.add("import " + ContextForJson.class.getName() + ";");
        result.add("import " + packageName + "." + interfaceName.className + ";");
        result.add("public class " + packageAndClassName.className + " implements HasJson<ContextForJson>, " + interfaceName.className + "{");
        result.addAll(Formating.indent(createFields()));
        result.addAll(Formating.indent(createConstructor()));
        result.addAll(Formating.indent(createLens()));
        result.add("");
        result.addAll(Formating.indent(makeJson()));
        result.add("");
        result.addAll(Formating.indent(createEquals()));
        result.addAll(Formating.indent(createHashcode()));
        result.add("}");
        return result;
    }

    public List<String> createFields() {
        return fields.map(nv -> "final " + nv.type.shortNameWithHasJson + " " + nv.name + ";//" + nv.type.interfaceDoms);
    }

    public List<String> createLens() {
        return fields.flatMap(fd -> new LensDom(fields, packageAndClassName.className, fd.type.shortNameWithHasJson, fd).createForClassOnServer());
    }


    public List<String> createConstructor() {
        List<String> result = new ArrayList<>();
        result.add("public " + packageAndClassName.className + "(" + fields.mapJoin(",", nv -> nv.type.shortNameWithHasJson + " " + nv.name) + "){");
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
        result.add(Formating.indent + "return " + fields.mapJoin(" && ", fd -> "Objects.equals(" + fd.name + ",other." + fd.name + ")") + ";");
        result.add("}");
        return result;
    }
    public List<String> createHashcode() {
        List<String> result = new ArrayList<>();
        result.add("@Override public int hashCode() {");
        result.add(Formating.indent + "return Objects.hash(" + fields.mapJoin(",", fd -> fd.name) + ");");
        result.add("}");
        return result;

    }

    String toJson(FieldDetails fd) {
        if (fd.type.shortName.equals("String")) {
            if (fd.templatedJson)
                return fd.name + ".replace(\"<host>\", context.protocolAndHost())";
            else
                return fd.name;
        }
        if (fd.type.embedded) {
            return fd.name + ".toJson(jsonTc,context)";
        } else
            return "((" + names.serverImplName(fd.type.shortName) + ")" + fd.name + ").toJson(jsonTc,context)";
    }
    List<String> makeJson() {
        List<String> result = new ArrayList<>();
        result.add("public <J> J toJson(JsonTC<J> jsonTc, ContextForJson context) {");
        result.add(Formating.indent + "return jsonTc.makeObject(" + fields.mapJoin(",", fd -> "\"" + fd.name + "\", " + toJson(fd)) + ");");
        result.add("}");
        return result;
    }


}
