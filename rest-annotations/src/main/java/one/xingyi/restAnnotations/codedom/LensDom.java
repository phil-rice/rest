package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.utils.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class LensDom {
    final static XingYiCallCodeDom callCodeDom = new XingYiCallCodeDom();
    final FieldList fieldList;
    private final String diamond;
    final String fromClassName;
    final String toClassName;
    final String Name;
    final String name;
    final FieldDetails fieldDetails;

    public LensDom(FieldList fieldList, String fromClassName, FieldDetails fieldDetails) {
        this.fieldList = fieldList;
        this.fromClassName = fromClassName;
        this.toClassName = fieldDetails.type;
        this.name = fieldDetails.name;
        this.fieldDetails = fieldDetails;
        this.Name = Strings.firstLetterUppercase(name);
        this.diamond = "<" + fromClassName + "," + toClassName + ">";
    }

    String lensHeader() {return "Lens" + diamond + " " + fromClassName + Name + "Lens";}
    public String lensString() {
        return "public static final " + lensHeader() + "=" +
                "Lens." + diamond + "create(" + fromClassName + "::" + name + ", " + fromClassName + "::with" + Name + ");//" + fieldDetails;
    }
    public String getStringDeclaration() {
        return "public " + toClassName + " " + name + "()";
    }
    public String getString() { return getStringDeclaration() + "{ return " + name + ";}"; }

    public String withString() {
        return withStringHeader() + "{ return " + fieldList.createConstructorCall(fromClassName) + ";}";
    }
    private String withStringHeader() {
        return "public " + fromClassName + " with" + Name + "(" + toClassName + " " + name + ")";
    }

    public List<String> createForClassOnServer() {
        return Arrays.asList("", lensString(), getString(), withString());
    }
    public List<String> createForClassOnClient(PackageAndClassName interfaceName) {
        return Arrays.asList("",
                lensHeader() + "=Lens." + diamond + "create(" + fromClassName + "::" + name + "," + fromClassName + "::with" + Name + ");",
                getStringDeclaration() + "{ return xingYi." + callCodeDom.xingyiGetCall(interfaceName,"Getter", this) + ".get(this); }",
                withStringHeader() + "{ return xingYi." + callCodeDom.xingyiGetCall(interfaceName,"Setter", this) + ".set(this, " + name + "); }//" + this);
    }
    public List<String> createForInterfacesOnServer(String interfaceName) {
        List<String> result = new ArrayList<>();
        boolean read = fieldDetails.shouldHaveRead(interfaceName);
        boolean write = fieldDetails.shouldHaveWrite(interfaceName);
        if (read)
            result.add(getStringDeclaration() + ";");
        if (write)
            result.add(withStringHeader() + ";");
//        if (read && write)
//            result.add("static " + lensHeader() + "();");
        if (result.size() > 0)
            result.add(0, "");
        return result;
    }

    @Override public String toString() {
        return "LensDom{" +
                "fieldList=..." +
                ", diamond='" + diamond + '\'' +
                ", fromClassName='" + fromClassName + '\'' +
                ", toClassName='" + toClassName + '\'' +
                ", Name='" + Name + '\'' +
                ", name='" + name + '\'' +
                ", fieldDetails=" + fieldDetails +
                '}';
    }
}
