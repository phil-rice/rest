package one.xingyi.restAnnotations.annotations;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.names.OpsNames;

import javax.lang.model.element.TypeElement;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class InterfaceData {
    public static InterfaceData create(INames names, TypeElement element) {
        boolean deprecated = element.getAnnotation(Deprecated.class) != null;
        PackageAndClassName serverName = new PackageAndClassName(element.asType().toString());
        return new InterfaceData(serverName, names.opsClientName(serverName), deprecated);
    }

    public final PackageAndClassName serverInterface;
    public final PackageAndClassName clientInterface;
    public final boolean deprecated;
}
