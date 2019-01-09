package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.Strings;

import java.util.Arrays;
import java.util.List;
public class LensDom {
    private FieldList fieldList;
    String fromClassName;
    String toClassName;
    String Name;
    String name;

    public LensDom(FieldList fieldList, String fromClassName, String toClassName, String name) {
        this.fieldList = fieldList;
        this.fromClassName = fromClassName;
        this.toClassName = toClassName;
        this.name = name;
        this.Name = Strings.firstLetterUppercase(name);
    }

    public String lensString() {
        String diamond = "<" + fromClassName + "," + toClassName + ">";
        return "public static final Lens" + diamond + " " + fromClassName + Name + "Lens=" +
                "Lens." + diamond + "create(" + fromClassName + "::" + name + ", " + fromClassName + "::with" + Name + ");";
    }
    public String getString() {
        return "public " + toClassName + " " + name + "(){ return " + name + ";}";
    }

    public String withString() {
        return "public " + fromClassName + " with" + Name + "(" + toClassName + " " + name + "){ return " + fieldList.createConstructorCall(fromClassName) + ";}";
    }

    public List<String> build() {
        return Arrays.asList(lensString(), getString(), withString());
    }
}
