package one.xingyi.restAnnotations.names;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;

@ToString
@EqualsAndHashCode
public class OpsNames {
    final INames names;
    public final PackageAndClassName opsInterface;
    public final PackageAndClassName opsServerCompanion;
    public final PackageAndClassName opsClientCompanion;
    public final EntityNames entityNames;


    public OpsNames(INames names, PackageAndClassName opsInterface, EntityNames entityNames) {
        this.names = names;
        this.opsInterface = opsInterface;
        this.entityNames = entityNames;
        this.opsServerCompanion = names.serverCompanionName(opsInterface);
        this.opsClientCompanion = names.clientCompanionName(opsInterface);
    }
}
