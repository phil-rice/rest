package one.xingyi.restAnnotations.names;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;
@EqualsAndHashCode
@ToString
public class EntityNames {
    final INames names;
    public final PackageAndClassName entityInterface;

    public final PackageAndClassName clientImplementation;
    public final PackageAndClassName serverImplementation;
    public final PackageAndClassName clientCompanion;
    public final PackageAndClassName serverCompanion;

    public EntityNames(INames names, String entityInterfaceNameString) {
        this.names = names;
        this.entityInterface = new PackageAndClassName(entityInterfaceNameString);
        this.clientImplementation = names.clientImplName(entityInterface);
        this.serverImplementation = names.serverImplName(entityInterface);
        this.clientCompanion = names.clientCompanionName(entityInterface);
        this.serverCompanion = names.serverCompanionName(entityInterface);
    }
}
