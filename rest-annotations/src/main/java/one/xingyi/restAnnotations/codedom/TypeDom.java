package one.xingyi.restAnnotations.codedom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.utils.Strings;
@ToString
@EqualsAndHashCode
public class TypeDom {
    final String fullName; //includes embedded
    final String fullNameOfEntity;
    final String shortName;
    final boolean embedded;


    public TypeDom(String fullName) {
        this.fullName = Strings.removeOptionalFirst("()", fullName);
        if (this.fullName.startsWith(Embedded.class.getName())) {
            this.fullNameOfEntity = Strings.extractFromOptionalEnvelope(Embedded.class.getName(), ">", this.fullName);
            this.shortName = Embedded.class.getSimpleName() + "<" + Strings.lastSegement("\\.", fullNameOfEntity) + ">";
            this.embedded = true;
        } else {
            this.fullNameOfEntity = this.fullName;
            this.shortName = Strings.lastSegement("\\.", this.fullName);
            this.embedded = false;
        }
    }

}
