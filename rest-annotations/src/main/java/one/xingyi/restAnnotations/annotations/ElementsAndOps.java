package one.xingyi.restAnnotations.annotations;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;
import one.xingyi.restAnnotations.utils.ListUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ElementsAndOps {
    public final List<ElementAndOps> list;

    public static ElementsAndOps create(Set<? extends Element> elements) {
        return new ElementsAndOps(elements.stream().filter(e -> ((Element) e).getKind() == ElementKind.INTERFACE).
                map(main -> {
                    List<String> interfaces = main.getEnclosedElements().stream().
                            map(e -> e.getAnnotation(XingYiField.class)).
                            filter(e -> e != null).
                            flatMap(e -> ListUtils.append(Arrays.asList(e.interfaces()),
                                    Arrays.asList(e.readInterfaces()),
                                    Arrays.asList(e.writeInterfaces())).stream()
                            ).collect(Collectors.toList());
                    return new ElementAndOps(new PackageAndClassName(((Element) main).asType().toString()), interfaces);
                }).
                collect(Collectors.toList()));
    }
    public List<String> allowedFor(String interfaceName, Class... othersAsArray) {
        List<String> others = ListUtils.map(Arrays.asList(othersAsArray), Class::getName);
        for (ElementAndOps elementAndOps : list) {
            if (elementAndOps.main.asString().equals(interfaceName))
                return ListUtils.append(ListUtils.append(others, elementAndOps.interfaces), ListUtils.map(list, e->e.main.asString()));//TODO currently allowing main as well
        }
        return ListUtils.append(others, ListUtils.map(list, e->e.main.asString()));
    }
}

