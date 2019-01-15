package one.xingyi.restAnnotations.codedom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.annotations.ElementsAndOps;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.entity.EmbeddedWithHasJson;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.utils.Strings;

import java.util.List;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class TypeDom {
    final String fullName; //includes embedded
    final String comment;
    final String fullNameOfEntity;
    final String shortName;
    final String shortNameWithHasJson;
    final String clientImplName;
    final boolean embedded;


    public static TypeDom create(INames names, String rawName, String comment) {
        String fullName = Strings.removeOptionalFirst("()", rawName);
        if (fullName.startsWith(Embedded.class.getName())) {
            String fullNameOfEntity = Strings.extractFromOptionalEnvelope(Embedded.class.getName(), ">", fullName);
            String justEntity = Strings.lastSegement("\\.", fullNameOfEntity);
            String shortName = Embedded.class.getSimpleName() + "<" + justEntity + ">";
            String shortNameWithHasJson = EmbeddedWithHasJson.class.getSimpleName() + "<" + justEntity + ">";
            String clientImplName = names.clientImplName(justEntity);//This is the place where I need to focus...
            boolean embedded = true;
            return new TypeDom(fullName, comment, fullNameOfEntity, shortName, shortNameWithHasJson, clientImplName, embedded);
        } else {
            String fullNameOfEntity = fullName;
            String shortName = Strings.lastSegement("\\.", fullName);
            String shortNameWithHasJson = shortName;
            String clientImplName = names.clientImplName(shortName);
            boolean embedded = false;
            return new TypeDom(fullName, comment, fullNameOfEntity, shortName, shortNameWithHasJson, clientImplName, embedded);
        }
    }
}
