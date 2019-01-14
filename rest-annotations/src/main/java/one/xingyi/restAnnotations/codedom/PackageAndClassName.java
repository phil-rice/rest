package one.xingyi.restAnnotations.codedom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.utils.Strings;

import java.util.function.Function;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class PackageAndClassName {
    public final String packageName;
    public final String className;

    public PackageAndClassName(String name) {
        packageName = Strings.allButLastSegment(".", name);
        className = name.substring(packageName.length()+1);
    }
    public PackageAndClassName mapName(Function<String, String> fn) {
        return new PackageAndClassName(packageName, fn.apply(className));
    }
    public String asString() {
        return packageName + "." + className;
    }
    public PackageAndClassName withName(String name) {
        return new PackageAndClassName(packageName, name);
    }
}
