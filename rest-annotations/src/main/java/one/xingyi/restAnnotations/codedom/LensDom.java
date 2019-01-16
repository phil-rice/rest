package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.Strings;

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

    public LensDom(FieldList fieldList, String fromClassName, String toClassName, FieldDetails fieldDetails) {
        this.fieldList = fieldList;
        this.fromClassName = fromClassName;
        this.toClassName = toClassName;
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
        return ListUtils.append(Arrays.asList("", getString()), Strings.useIf(!fieldDetails.readOnly, lensString()), Strings.useIf(!fieldDetails.readOnly, withString()));
    }

    String createLensOnClient() {return "public " + lensHeader() + "(){ return xingYi." + callCodeDom.xingyiGetCall("Lens", this) + ";}";}
    String createReadOnClient() {return getStringDeclaration() + "{ return " + fromClassName + Name + "Lens().get(this); }";}
    List<String> createWithOnClient() {return Strings.useIf(!fieldDetails.readOnly, withStringHeader() + "{ return " + fromClassName + Name + "Lens().set(this, " + name + "); }");}

    public List<String> createForClassOnClient() { return ListUtils.append(Arrays.asList("", createLensOnClient(), createReadOnClient()), createWithOnClient()); }

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
