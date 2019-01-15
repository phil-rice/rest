package one.xingyi.restAnnotations.codedom;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.annotations.ElementsAndOps;
import one.xingyi.restAnnotations.annotations.XingYiField;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.OptionalUtils;
import one.xingyi.restAnnotations.utils.Strings;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import java.util.ArrayList;
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
    final boolean templatedJson;
    public FieldDetails(LoggerAdapter log, TypeDom type, String name, List<String> readInterfaces, List<String> writeInterfaces, List<String> readWriteInterfaces, String lensName, Optional<String> javascript, boolean deprecated, boolean templatedJson) {
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
        this.templatedJson = templatedJson;
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

    static String getLensName(String interfaceName, String name, String toClassName, Optional<String> lensName) {
        return lensName.orElse(interfaceName + "_" + name) + "_" + toClassName.replace("<", "").replace(">", "");
    }



    public boolean isPresent(String interfaceName) {
        List<String> result = new ArrayList<>();
        boolean read = shouldHaveRead(interfaceName);
        boolean write = shouldHaveWrite(interfaceName);
        return read || write;
    }

    public static FieldDetails create(LoggerAdapter log, INames names, ElementsAndOps elementsAndOps, String interfaceName, Element element) {
//        element.accept(new ElementVisitor<String, Void>() {});
        String rawType = Strings.removeOptionalFirst("()", element.asType().toString());
        String name = element.getSimpleName().toString();
        TypeDom typeDom =  TypeDom.create(names,rawType, elementsAndOps.toString());

        List<String> allowed = elementsAndOps.allowedFor(typeDom.fullNameOfEntity, String.class);
        if (!allowed.contains(typeDom.fullNameOfEntity))
            log.warning(element, name + " cannot return a " + typeDom.fullNameOfEntity + " it can only return " + allowed+"\n" + elementsAndOps + "\n" +rawType);

        XingYiField xingYiField = element.getAnnotation(XingYiField.class);
        ExecutableElement executableElement = (ExecutableElement) element;
        if (executableElement.getParameters().size() > 0)
            log.error(element, name + " should not have parameters ");


        //        log.info("ng field details. InterfaceName is [" + interfaceName + "] name is [" + name + "] rawType is" + "[" + rawType + "] typeDom is " + typeDom);
        if (xingYiField == null)
            return new FieldDetails(log, typeDom, name, Arrays.asList(), Arrays.asList(), Arrays.asList(), getLensName(interfaceName, name, typeDom.shortName, Optional.empty()), Optional.empty(), false, false);
        else
            return new FieldDetails(log, typeDom, name,
                    Arrays.asList(xingYiField.readInterfaces()), Arrays.asList(xingYiField.writeInterfaces()), Arrays.asList(xingYiField.interfaces()),
                    getLensName(interfaceName, name, typeDom.shortName, OptionalUtils.fromString(xingYiField.lens())), OptionalUtils.fromString(xingYiField.javascript()), xingYiField.deprecated(), xingYiField.templatedJson());
    }


}
