package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.utils.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class LensDom {
    final FieldList fieldList;
    final String fromClassName;
    final String toClassName;
    final String Name;
    final String name;
    private FieldDetails fieldDetails;

    public LensDom(FieldList fieldList, String fromClassName, FieldDetails fieldDetails) {
        this.fieldList = fieldList;
        this.fromClassName = fromClassName;
        this.toClassName = fieldDetails.type;
        this.name = fieldDetails.name;
        this.fieldDetails = fieldDetails;
        this.Name = Strings.firstLetterUppercase(name);
    }

    public String lensString() {
        String diamond = "<" + fromClassName + "," + toClassName + ">";
        return "public static final Lens" + diamond + " " + fromClassName + Name + "Lens=" +
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
    public List<String> createForInterfacesOnServer(String interfaceName) {
        List<String> result = new ArrayList<>();
        boolean read = fieldDetails.shouldHaveRead(interfaceName);
        boolean write = fieldDetails.shouldHaveWrite(interfaceName);
        if (read)
            result.add(getStringDeclaration() + ";");
        if (write)
            result.add(withStringHeader() + ";");
        if (read && write)
            result.add(lensString());
        return result;
    }
}
