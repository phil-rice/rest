package one.xingyi.restAnnotations.codedom;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.annotations.XingYiField;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.OptionalUtils;

import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@ToString
@EqualsAndHashCode

public class FieldDetails {
    @ToString.Exclude
    final LoggerAdapter log;
    final TypeDom type;
    final String name;
    final List<String> readInterfaces;
    final List<String> writeInterfaces;
    final List<String> readWriteInterfaces;
    final String lensName;
    final Optional<String> javascript;
    final boolean deprecated;
    public FieldDetails(LoggerAdapter log, TypeDom type, String name, List<String> readInterfaces, List<String> writeInterfaces, List<String> readWriteInterfaces, String lensName, Optional<String> javascript, boolean deprecated) {
        this.log = log;
        this.type = type;
        this.name = name;
        this.readInterfaces = readInterfaces;
        this.writeInterfaces = writeInterfaces;
        this.readWriteInterfaces = readWriteInterfaces;
        this.lensName = lensName;
        this.javascript = javascript;
        this.deprecated = deprecated;
//        log.info("In field details " + this);
    }
    public List<String> allInterfaces() {
        return ListUtils.unique(ListUtils.append(readInterfaces, writeInterfaces, readWriteInterfaces));
    }
    public boolean shouldHaveRead(String interfaceName) {
        return readWriteInterfaces.contains(interfaceName) || readInterfaces.contains(interfaceName);
    }
    public boolean shouldHaveWrite(String interfaceName) {
        return readWriteInterfaces.contains(interfaceName) || writeInterfaces.contains(interfaceName);
    }

//    public FieldDetails mapType(Function<String, String> fn) {
//        return new FieldDetails(log, fn.apply(type), name, readInterfaces, writeInterfaces, readWriteInterfaces, lensName, javascript, deprecated);
//    }

    static String getLensName(String interfaceName, String name, String toClassName, Optional<String> lensName) {
        return lensName.orElse(interfaceName + "_" + name) + "_" + toClassName.replace("<","").replace(">","");
    }

    public static FieldDetails create(LoggerAdapter log, INames names, String interfaceName, Element element) {
        String rawType = element.asType().toString();
        TypeDom typeDom = new TypeDom(names, rawType);
        XingYiField xingYiField = element.getAnnotation(XingYiField.class);
        String name = element.getSimpleName().toString();
//        log.info("Making field details. InterfaceName is [" + interfaceName + "] name is [" + name + "] rawType is" + "[" + rawType + "] typeDom is " + typeDom);
        if (xingYiField == null)
            return new FieldDetails(log, typeDom, name, Arrays.asList(), Arrays.asList(), Arrays.asList(), getLensName(interfaceName, name, typeDom.shortName, Optional.empty()), Optional.empty(), false);
        else
            return new FieldDetails(log, typeDom, name,
                    Arrays.asList(xingYiField.readInterfaces()), Arrays.asList(xingYiField.writeInterfaces()), Arrays.asList(xingYiField.interfaces()),
                    getLensName(interfaceName, name, typeDom.shortName, OptionalUtils.fromString(xingYiField.lens())), OptionalUtils.fromString(xingYiField.javascript()), xingYiField.deprecated());
    }


}
