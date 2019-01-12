package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.annotations.XingYiField;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.OptionalUtils;
import one.xingyi.restAnnotations.utils.Strings;

import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FieldDetails {
    final String type;
    final String name;
    final List<String> readInterfaces;
    final List<String> writeInterfaces;
    final List<String> readWriteInterfaces;
    final List<String> allInterfaces;
    final Optional<String> lensName;
    final Optional<String> javascript;
    final boolean deprecated;

    public FieldDetails(String type, String name, List<String> readInterfaces, List<String> writeInterfaces, List<String> readWriteInterfaces, Optional<String> lensName, Optional<String> javascript, boolean deprecated) {
        this.type = type;
        this.name = name;
        this.readInterfaces = readInterfaces;
        this.writeInterfaces = writeInterfaces;
        this.readWriteInterfaces = readWriteInterfaces;
        this.lensName = lensName;
        this.javascript = javascript;
        this.deprecated = deprecated;
        this.allInterfaces = ListUtils.unique(ListUtils.append(readInterfaces, writeInterfaces, readWriteInterfaces));
    }
    public boolean shouldHaveRead(String interfaceName) {
        return readWriteInterfaces.contains(interfaceName) || readInterfaces.contains(interfaceName);
    }
    public boolean shouldHaveWrite(String interfaceName) {
        return readWriteInterfaces.contains(interfaceName) || writeInterfaces.contains(interfaceName);
    }

    public FieldDetails mapType(Function<String, String> fn) {
        return new FieldDetails(fn.apply(type), name, readInterfaces, writeInterfaces, readWriteInterfaces, lensName, javascript, deprecated);
    }

    @Override public String toString() {
        return "FieldDetails{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", readInterfaces=" + readInterfaces +
                ", writeInterfaces=" + writeInterfaces +
                ", readWriteInterfaces=" + readWriteInterfaces +
                ", lensName=" + lensName +
                ", javascript=" + javascript +
                ", deprecated=" + deprecated +
                '}';
    }
    public static FieldDetails create(Element element) {
        String rawType = element.asType().toString();
        XingYiField xingYiField = element.getAnnotation(XingYiField.class);
        String cleaned = Strings.removeOptionalFirst("()", rawType);
        String name = element.getSimpleName().toString();
        if (xingYiField == null)
            return new FieldDetails(cleaned, name, Arrays.asList(), Arrays.asList(), Arrays.asList(), Optional.empty(), Optional.empty(), false);
        else
            return new FieldDetails(cleaned, name,
                    Arrays.asList(xingYiField.readInterfaces()), Arrays.asList(xingYiField.writeInterfaces()), Arrays.asList(xingYiField.interfaces()),
                    OptionalUtils.fromString(xingYiField.lens()), OptionalUtils.fromString(xingYiField.javascript()), xingYiField.deprecated());
    }


}
