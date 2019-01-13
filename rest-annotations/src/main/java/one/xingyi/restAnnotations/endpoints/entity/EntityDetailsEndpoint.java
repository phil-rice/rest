package one.xingyi.restAnnotations.endpoints.entity;

import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.endpoints.EndpointAcceptor1;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface EntityDetailsEndpoint {
    EndpointAcceptor1<EntityDetailsRequest> acceptor = EndpointAcceptor1.justOneThing("get", EntityDetailsRequest::new);

    static EndPoint entityDetailsEndPoint(Function<EntityDetailsRequest, CompletableFuture<EntityDetailsResponse>> fn) { return EndPoint.simple(acceptor, fn); }
}