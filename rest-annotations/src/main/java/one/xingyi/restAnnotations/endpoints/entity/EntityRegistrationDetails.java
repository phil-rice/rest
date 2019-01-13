package one.xingyi.restAnnotations.endpoints.entity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.entity.Companion;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EntityRegistrationDetails {
    final String entityName;
    final String urlPattern;
    final Companion<?, ?> companion;
}
