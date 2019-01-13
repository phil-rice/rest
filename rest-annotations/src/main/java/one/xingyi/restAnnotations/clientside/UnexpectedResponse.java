package one.xingyi.restAnnotations.clientside;
import one.xingyi.restAnnotations.http.ServiceResponse;
public class UnexpectedResponse extends RuntimeException {
    public UnexpectedResponse(ServiceResponse response) {
        super(response.toString());
    }
    public UnexpectedResponse(String message, ServiceResponse response) {
        super(message + response);
    }
}