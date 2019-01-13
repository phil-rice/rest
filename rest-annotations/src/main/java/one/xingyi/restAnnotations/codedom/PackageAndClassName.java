package one.xingyi.restAnnotations.codedom;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.function.Function;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class PackageAndClassName {
    public final String packageName;
    public  final String className;


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
