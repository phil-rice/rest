package one.xingyi.restcore.endpoints.entity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restcore.endpoints.EndpointRequest;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EntityDetailsRequest implements EndpointRequest {
    final String entityName;
}
