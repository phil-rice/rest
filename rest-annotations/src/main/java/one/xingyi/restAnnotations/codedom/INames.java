package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.utils.Strings;

import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.List;

public interface INames {

    PackageAndClassName get(Element element);
    List<String> validateEntityName(PackageAndClassName entityName);

    PackageAndClassName interfaceName(PackageAndClassName packageAndClassName);
    PackageAndClassName serverImplName(PackageAndClassName packageAndClassName);
    PackageAndClassName serverCompanionName(PackageAndClassName packageAndClassName);
    PackageAndClassName clientImplName(PackageAndClassName packageAndClassName);


    static INames defaultNames = new DefaultNames();
}


class DefaultNames implements INames {

    @Override public PackageAndClassName get(Element element) {
        String packageName = Strings.allButLastSegment(".", element.asType().toString());
        String interfaceName = element.getSimpleName().toString();
        return new PackageAndClassName(packageName, interfaceName);

    }
    @Override public List<String> validateEntityName(PackageAndClassName packageAndClassName) {
        return packageAndClassName.className.startsWith("I") ? Arrays.asList() : Arrays.asList("Doesn't start with an 'I'");
    }
    //    @Override public List<String> validateOpsName(PackageAndClassName companionName) {
//        String opsName = companionName.className;
//        return opsName.startsWith("I") ? Arrays.asList() : Arrays.asList("'" + opsName + "' doesn't start with an 'i'");
//
//    }
    @Override public PackageAndClassName interfaceName(PackageAndClassName packageAndClassName) {
        return packageAndClassName.mapName(e -> e);
    }
    @Override public PackageAndClassName serverImplName(PackageAndClassName packageAndClassName) {
        return packageAndClassName.mapName(e -> e.substring(1));
    }
    @Override public PackageAndClassName serverCompanionName(PackageAndClassName packageAndClassName) {
        return packageAndClassName.mapName(e -> e.substring(1) + "ServerCompanion");
    }
    @Override public PackageAndClassName clientImplName(PackageAndClassName packageAndClassName) {
        return packageAndClassName.mapName(e -> e.substring(1) + "ClientImpl");
    }
}