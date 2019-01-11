package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.utils.Strings;

import java.util.Arrays;
import java.util.List;
public class LensDom {
    final FieldList fieldList;
    final String fromClassName;
    final String toClassName;
    final String Name;
    final String name;
    private TypeName typeName;

    public LensDom(FieldList fieldList, String fromClassName, TypeName typeName) {
        this.fieldList = fieldList;
        this.fromClassName = fromClassName;
        this.toClassName = typeName.type;
        this.name = typeName.name;
        this.typeName = typeName;
        this.Name = Strings.firstLetterUppercase(name);
    }

    public String lensString() {
        String diamond = "<" + fromClassName + "," + toClassName + ">";
        return "public static final Lens" + diamond + " " + fromClassName + Name + "Lens=" +
                "Lens." + diamond + "create(" + fromClassName + "::" + name + ", " + fromClassName + "::with" + Name + ");//" + typeName;
    }
    public String getString() {
        return "public " + toClassName + " " + name + "(){ return " + name + ";}";
    }

    public String withString() {
        return "public " + fromClassName + " with" + Name + "(" + toClassName + " " + name + "){ return " + fieldList.createConstructorCall(fromClassName) + ";}";
    }

    public List<String> build() {
        return Arrays.asList("", lensString(), getString(), withString());
    }
}
