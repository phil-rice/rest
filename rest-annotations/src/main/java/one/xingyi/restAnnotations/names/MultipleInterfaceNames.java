package one.xingyi.restAnnotations.names;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;
@EqualsAndHashCode
@ToString
public class MultipleInterfaceNames {
    final INames names;
    public final PackageAndClassName multipleInterfaceName;
    public final PackageAndClassName serverImplementationName;
    public final PackageAndClassName serverCompanionName;
    private final PackageAndClassName entityImplementationName;

    public MultipleInterfaceNames(INames names, String multipleInterfaceString, String entityString) {
        this.names = names;
        this.multipleInterfaceName = new PackageAndClassName(multipleInterfaceString);
        this.serverImplementationName = names.serverImplName(multipleInterfaceName);
        this.serverCompanionName = names.serverCompanionName(multipleInterfaceName);
        this.entityImplementationName = names.serverImplName(new PackageAndClassName(entityString));
    }
}
