package one.xingyi.restAnnotations.endpoints;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.utils.OptionalUtils;

import java.util.Optional;
import java.util.function.Function;
public interface EndpointAcceptor1<From> extends Function<ServiceRequest, Optional<From>> {

    static <From> EndpointAcceptor1<From> justOneThing(String method, Function<String, From> fn) {
        return sr -> OptionalUtils.from(sr.segmentsCount() == 2 && method.equalsIgnoreCase(sr.method), () -> fn.apply(sr.lastSegment()));
    }
}
