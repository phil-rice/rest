package one.xingyi.restAnnotations.marshelling;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ContextForJson {
    final ServiceRequest serviceRequest;
    public String protocolAndHost() { return serviceRequest.header("host").map(h -> "http://" + h).orElse("");}
}
