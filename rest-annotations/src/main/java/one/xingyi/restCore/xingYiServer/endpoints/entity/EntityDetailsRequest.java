package one.xingyi.restCore.xingYiServer.endpoints.entity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restCore.xingYiServer.endpoints.EndpointRequest;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EntityDetailsRequest implements EndpointRequest {
    final String entityName;
}
