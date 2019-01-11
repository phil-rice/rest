package one.xingyi.restAnnotations.codedom;

import java.util.function.Function;
public class PackageAndClassName {
    final String packageName;
    final String className;

    public PackageAndClassName(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
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
