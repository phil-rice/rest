package one.xingyi.restAnnotations.entity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.entity.Companion;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EntityRegistrationDetails {
    public final String entityName;
    public final String urlPattern;
    public final Companion<?, ?> companion;
}
