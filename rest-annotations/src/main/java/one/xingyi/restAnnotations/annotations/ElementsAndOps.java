package one.xingyi.restAnnotations.annotations;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.OptionalUtils;
import one.xingyi.restAnnotations.utils.Strings;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ElementsAndOps {
    public final List<ElementAndOps> list;

    public Optional<ElementAndOps> find(String interfaceName) { return OptionalUtils.find(list, e -> e.main.asString().equalsIgnoreCase(interfaceName)); }


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

                    List<String> returnedTypes = ListUtils.unique(ListUtils.map(main.getEnclosedElements(), e -> Strings.removeOptionalFirst("()", e.asType().toString())));

                    return new ElementAndOps(new PackageAndClassName(main.asType().toString()), interfaces, returnedTypes);
                }).
                collect(Collectors.toList()));
    }
    public List<String> allowedFor(String interfaceName, Class... othersAsArray) {
        List<String> others = ListUtils.map(Arrays.asList(othersAsArray), Class::getName);
        return ListUtils.append(others,
                OptionalUtils.fold(find(interfaceName), () -> Arrays.asList(), found -> found.interfaces),
                ListUtils.map(list, e -> e.main.asString()));
    }
}

