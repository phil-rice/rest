package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.utils.Strings;

import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.List;

public interface INames {

    PackageAndClassName get(Element element);
    List<String> validateEntityName(PackageAndClassName entityName);

    PackageAndClassName interfaceName(PackageAndClassName packageAndClassName);
    String serverImplName(String interfaceName);
    default PackageAndClassName serverImplName(PackageAndClassName packageAndClassName) { return packageAndClassName.mapName(this::serverImplName); }
    PackageAndClassName serverCompanionName(PackageAndClassName packageAndClassName);
    String clientImplName(String interfaceName);
    default PackageAndClassName clientImplName(PackageAndClassName interfaceName) {return interfaceName.mapName(this::clientImplName);}

    String clientCompanionName(String interfaceName);
    default PackageAndClassName clientCompanionName(PackageAndClassName interfaceName) {return interfaceName.mapName(this::clientCompanionName);}


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
    @Override public String serverImplName(String interfaceName) {
        return interfaceName.substring(1);
    }
    @Override public PackageAndClassName serverCompanionName(PackageAndClassName packageAndClassName) {
        return packageAndClassName.mapName(e -> e.substring(1) + "ServerCompanion");
    }
    @Override public String clientImplName(String interfaceName) {
        return interfaceName.substring(1) + "ClientImpl";
    }
    @Override public String clientCompanionName(String interfaceName) { return interfaceName.substring(1) + "ClientCompanion"; }
}