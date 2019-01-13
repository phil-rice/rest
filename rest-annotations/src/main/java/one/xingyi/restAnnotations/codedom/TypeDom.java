package one.xingyi.restAnnotations.codedom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.entity.EmbeddedWithHasJson;
import one.xingyi.restAnnotations.utils.Strings;
@ToString
@EqualsAndHashCode
public class TypeDom {
    final String fullName; //includes embedded
    final String fullNameOfEntity;
    final String shortName;
    final String shortNameWithHasJson;
    final String clientImplName;
    final boolean embedded;


    public TypeDom(INames names, String fullName) {
        this.fullName = Strings.removeOptionalFirst("()", fullName);
        if (this.fullName.startsWith(Embedded.class.getName())) {
            this.fullNameOfEntity = Strings.extractFromOptionalEnvelope(Embedded.class.getName(), ">", this.fullName);
            String justEntity = Strings.lastSegement("\\.", fullNameOfEntity);
            this.shortName = Embedded.class.getSimpleName() + "<" + justEntity + ">";
            this.shortNameWithHasJson = EmbeddedWithHasJson.class.getSimpleName() + "<" + justEntity + ">";
            this.clientImplName=names.clientImplName(justEntity);
            this.embedded = true;
        } else {
            this.fullNameOfEntity = this.fullName;
            this.shortName = Strings.lastSegement("\\.", this.fullName);
            this.shortNameWithHasJson = shortName;
            this.clientImplName = names.clientImplName(shortName);
            this.embedded = false;
        }
    }

}
