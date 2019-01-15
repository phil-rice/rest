package one.xingyi.restAnnotations.names;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;
@EqualsAndHashCode
@ToString
public class MultipleInterfaceNames {
    final INames names;
    public final PackageAndClassName multipleInterfaceName;
    public final PackageAndClassName multipleInterfacesClientImplName;
    public final PackageAndClassName multipleInterfacesClientCompanion;
    public final EntityNames entityNames;

    public MultipleInterfaceNames(INames names, String multipleInterfaceString, String entityString) {
        this.names = names;
        this.multipleInterfaceName = new PackageAndClassName(multipleInterfaceString);
        this.multipleInterfacesClientImplName = names.clientImplName(multipleInterfaceName);
        this.multipleInterfacesClientCompanion = names.clientCompanionName(multipleInterfaceName);
        this.entityNames = new EntityNames(names, entityString);
    }
}
