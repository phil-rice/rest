package one.xingyi.restAnnotations.endpoints.entity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.endpoints.EndpointAcceptor1;
import one.xingyi.restAnnotations.endpoints.EndpointResponse;
import one.xingyi.restAnnotations.http.ServiceResponse;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EntityDetailsResponse implements EndpointResponse {
    final EntityRegistrationDetails details;

    static EndpointAcceptor1<EntityDetailsRequest> matcher = EndpointAcceptor1.justOneThing("get", EntityDetailsRequest::new);
    @Override public ServiceResponse serviceResponse() {
        return ServiceResponse.html(200, "got to the entity details response\n" + details);
    }
}
