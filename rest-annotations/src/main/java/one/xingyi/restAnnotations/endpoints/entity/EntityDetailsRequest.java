package one.xingyi.restAnnotations.endpoints.entity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.endpoints.EndpointRequest;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EntityDetailsRequest implements EndpointRequest {
    final String entityName;
}
