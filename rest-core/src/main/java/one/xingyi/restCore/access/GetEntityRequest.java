package one.xingyi.restcore.access;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.endpoints.EndpointRequest;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class GetEntityRequest implements EndpointRequest {
    final String id;
}
