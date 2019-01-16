package one.xingyi.restAnnotations.codedom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.annotations.ElementsAndOps;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.entity.EmbeddedWithHasJson;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.utils.OptionalUtils;
import one.xingyi.restAnnotations.utils.Strings;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class TypeDom {
    final String fullName; //includes embedded
    //    final String comment;
    final String fullNameOfEntity;
    final String shortName;
    final String shortNameWithHasEmbeddedJson;
    final String shortNameWithHasJson;
    final Optional<EntityNames> optEntityName;
    final boolean embedded;
    final boolean primitive;


    public static TypeDom create(INames names, String rawName) {
        String fullName = Strings.removeOptionalFirst("()", rawName);
        if (fullName.startsWith(Embedded.class.getName())) {
            String fullNameOfEntity = Strings.extractFromOptionalEnvelope(Embedded.class.getName(), ">", fullName);
            String justEntity = Strings.lastSegement("\\.", fullNameOfEntity);
            String shortName = Embedded.class.getSimpleName() + "<" + justEntity + ">";
            String shortNameWithHasEmbeddedJson = EmbeddedWithHasJson.class.getSimpleName() + "<" + justEntity + ">";
            String shortNameWithHasJson = Embedded.class.getSimpleName() + "<" + justEntity + ">";
            EntityNames entityName = new EntityNames(names, fullNameOfEntity);//This is the place where I need to focus...
            boolean embedded = true;
            return new TypeDom(fullName, fullNameOfEntity, shortName, shortNameWithHasEmbeddedJson, shortNameWithHasJson, Optional.of(entityName), embedded, false);
        } else {
            String fullNameOfEntity = fullName;
            String shortName = Strings.lastSegement("\\.", fullName);
            String shortNameWithHasEmbeddedJson = shortName;
            String shortNameWithHasJson = shortName;
            String clientImplName = names.clientImplName(shortName);
            boolean embedded = false;
            boolean primitive = shortName.equalsIgnoreCase("String");
            return new TypeDom(fullName, fullNameOfEntity, shortName, shortNameWithHasEmbeddedJson, shortNameWithHasJson, OptionalUtils.from(!primitive, () -> new EntityNames(names, fullNameOfEntity)), embedded, primitive);
        }
    }
}
