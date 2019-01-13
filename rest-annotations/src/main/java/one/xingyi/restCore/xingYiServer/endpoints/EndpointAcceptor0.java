package one.xingyi.restCore.xingYiServer.endpoints;
import one.xingyi.restAnnotations.http.ServiceRequest;

import java.util.function.Function;
public interface EndpointAcceptor0 extends Function<ServiceRequest, Boolean> {

    static <From> EndpointAcceptor0 exact(String method, String path) {
        return sr -> sr.method.equalsIgnoreCase(method) && sr.url.equalsIgnoreCase(path);
    }
}
