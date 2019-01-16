package one.xingyi.restAnnotations.annotations;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.lang.model.element.TypeElement;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class InterfaceData {
    public static InterfaceData create(TypeElement element) {
        boolean deprecated = element.getAnnotation(Deprecated.class) != null;
        return new InterfaceData(element.asType().toString(), deprecated);
    }

    public final String name;
    public final boolean deprecated;
}
