package one.xingyi.restAnnotations.codedom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.names.EntityNames;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class EntityDetails {
    final EntityNames entityNames;
    final boolean hasUrlPattern;

}
