package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.javascript.XingYiDomain;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.optics.Lens;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class EntityOnClientClassDom {
    public final FieldList fields;
    public final PackageAndClassName interfaceName;
    private LoggerAdapter log;
    public INames names;
    public final PackageAndClassName packageAndClassName;

    public EntityOnClientClassDom(LoggerAdapter log, INames names, EntityNames entityNames, FieldList fields) {
        this.log = log;
        this.names = names;
        this.packageAndClassName = entityNames.clientImplementation;
        this.interfaceName = entityNames.entityInterface;
        this.fields = fields;
    }


    List<String> interfaces() {
        return ListUtils.unique(ListUtils.add(fields.flatMap(fd -> fd.allInterfaces()), interfaceName.className));
    }

    public List<String> createClass() {
        String packageName = packageAndClassName.packageName;
        ArrayList<String> result = new ArrayList<>();
        result.add("package " + packageName + ";");
        result.addAll(fields.createImports());
        result.add("import " + IXingYi.class.getName() + ";");
        result.add("import " + Lens.class.getName() + ";");
        result.add("import " + XingYiDomain.class.getName() + ";");
        result.add("import " + Embedded.class.getName() + ";");
        result.add("import " + packageName + "." + interfaceName.className + ";");
        result.add("public class " + packageAndClassName.className + " extends XingYiDomain implements " + ListUtils.join(interfaces(), ",") + "{");
        result.addAll(Formating.indent(createFields()));
        result.addAll(Formating.indent(createConstructor()));
        result.addAll(Formating.indent(createLens()));
        result.add("}");
        return result;
    }

    public List<String> createFields() {
        return Arrays.asList("final IXingYi xingYi;");
    }

    public List<String> createLens() {
        return fields.flatMap(fd -> {
            return new LensDom(fields, packageAndClassName.className, fd.type.shortName, fd).createForClassOnClient();
        });
    }

    public List<String> createConstructor() {
        return Arrays.asList("public " + packageAndClassName.className + "(Object mirror, IXingYi xingYi){ super(mirror); this.xingYi = xingYi;}");
    }
}
