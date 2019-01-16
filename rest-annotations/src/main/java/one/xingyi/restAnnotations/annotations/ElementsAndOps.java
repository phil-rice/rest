package one.xingyi.restAnnotations.annotations;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.MapUtils;
import one.xingyi.restAnnotations.utils.OptionalUtils;
import one.xingyi.restAnnotations.utils.Strings;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ElementsAndOps {
    public final List<ElementAndOps> list;

    public Optional<ElementAndOps> find(String interfaceName) { return OptionalUtils.find(list, e -> e.main.asString().equalsIgnoreCase(interfaceName)); }


    public static ElementsAndOps create(LoggerAdapter log, Set<? extends Element> xingYiElements, Set<? extends Element> xingYiopsElements) {
        Map<String, List<String>> map = new HashMap<>();
        for (Element element : xingYiopsElements) {
            MapUtils.add(map, ProcessXingYiOpsAnnotation.findEntity(Optional.empty(), (TypeElement) element), element.asType().toString());
        }
//        log.info("in create " + map);

        return new ElementsAndOps(xingYiElements.stream().filter(e -> ((Element) e).getKind() == ElementKind.INTERFACE).
                map(main -> {
                    String key = ((Element) main).asType().toString();
//                    log.info("In create key is " + key);
                    List<String> interfaces = map.getOrDefault(key, Arrays.asList());
//                    log.info("In create interfaces are " + interfaces);
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

