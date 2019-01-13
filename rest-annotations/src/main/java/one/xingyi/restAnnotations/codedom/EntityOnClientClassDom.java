package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.javascript.XingYiDomain;
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

    public EntityOnClientClassDom(LoggerAdapter log,INames names, PackageAndClassName packageAndClassName, PackageAndClassName interfaceName, FieldList fields) {
        this.log = log;
        this.names = names;
        this.packageAndClassName = packageAndClassName;
        this.interfaceName = interfaceName;
        this.fields = fields;
    }

//    EntityOnClientClassDom withFields(FieldList fields) {
//        return new EntityOnClientClassDom(names, packageAndClassName, interfaceName, fields);
//    }
//    EntityOnClientClassDom withPackageAndClassName(PackageAndClassName packageAndClassName, PackageAndClassName interfaceName) {
//        return new EntityOnClientClassDom(names, packageAndClassName, interfaceName, fields);
//    }

//    List<String> nestedOps() { return ListUtils.unique(fields.flatMap(tn -> tn.allInterfaces())); }
//    public List<OpsInterfaceClassDom> nested() {
//        return ListUtils.map(nestedOps(), opsName -> new OpsInterfaceClassDom(packageAndClassName.withName(opsName), interfaceName, fields));
//    }

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
//        result.addAll(Formating.indent(createDomainMaker()));
        result.addAll(Formating.indent(createFields()));
        result.addAll(Formating.indent(createConstructor()));
        result.addAll(Formating.indent(createLensForServerClass()));
        result.add("}");
        return result;
    }
//    private List<String> createDomainMaker() {
//        return Arrays.asList("static IDomainMaker<" + companionName.className + ">domainMaker(){return (m,x)->new " + companionName.className + "(m, x);}");
//    }


    public List<String> createFields() {
        return Arrays.asList("final IXingYi xingYi;");
    }

    public List<String> createLensForServerClass() {
        return fields.flatMap(fd -> {
            log.info("making lens for server class Field details are" + fd);
            String targetClassname = names.clientImplName(packageAndClassName.withName(fd.type.shortName)).className;
            return new LensDom(fields, packageAndClassName.className, fd).createForClassOnClient(interfaceName, targetClassname);
        });
    }

    public List<String> createConstructor() {
        return Arrays.asList("public " + packageAndClassName.className + "(Object mirror, IXingYi xingYi){ super(mirror); this.xingYi = xingYi;}");
    }
}
