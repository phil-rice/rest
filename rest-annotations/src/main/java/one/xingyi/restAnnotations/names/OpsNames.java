package one.xingyi.restAnnotations.names;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;

@ToString
@EqualsAndHashCode
public class OpsNames {
    final INames names;
    public final PackageAndClassName opsServerInterface;
    public final PackageAndClassName opsClientInterface;
    public final PackageAndClassName opsServerCompanion;
    public final PackageAndClassName opsClientCompanion;
    public final EntityNames entityNames;


    public OpsNames(INames names, PackageAndClassName opsServerInterface, EntityNames entityNames) {
        this.names = names;
        if (!opsServerInterface.className.endsWith("Ops"))
            throw new RuntimeException("Somehow this got called with an interface serverName that didn't end in Ops: " + opsServerInterface);
        this.opsServerInterface = opsServerInterface;
        this.opsClientInterface=names.opsClientName(opsServerInterface);
        this.entityNames = entityNames;
        this.opsServerCompanion = names.serverCompanionName(opsServerInterface);
        this.opsClientCompanion = names.clientCompanionName(opsClientInterface);
    }
}
