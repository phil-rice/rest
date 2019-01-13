package one.xingyi.restcore.endpoints.entity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.marshelling.HasJson;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restcore.endpoints.EndpointAcceptor1;
import one.xingyi.restcore.endpoints.EndpointResponse;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EntityDetailsResponse implements EndpointResponse, HasJson {
    final EntityRegistrationDetails details;

    static EndpointAcceptor1<EntityDetailsRequest> matcher = EndpointAcceptor1.justOneThing("get", EntityDetailsRequest::new);
    @Override public ServiceResponse serviceResponse() {
        return ServiceResponse.html(200, "got to the entity details response\n" + details);
    }
    @Override public <J> J toJson(JsonTC<J> jsonTc) {
        return null;
    }
}
