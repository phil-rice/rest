package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.Strings;

import javax.lang.model.element.Element;
import java.util.function.Function;
public class TypeName {
    final String type;
    final String name;

    public TypeName(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public TypeName mapType(Function<String,String> fn){
        return new TypeName(fn.apply(type), name);
    }

    public static TypeName create(Element element) {
        String rawType = element.asType().toString();
        String cleaned = Strings.removeOptionalFirst("()", rawType);
        return new TypeName(cleaned, element.getSimpleName().toString());
    }
    @Override public String toString() {
        return "TypeName{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
