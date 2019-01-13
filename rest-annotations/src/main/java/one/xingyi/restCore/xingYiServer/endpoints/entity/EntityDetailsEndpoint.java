package one.xingyi.restCore.xingYiServer.endpoints.entity;

import one.xingyi.restCore.xingYiServer.endpoints.EndPoint;
import one.xingyi.restCore.xingYiServer.endpoints.EndpointAcceptor1;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface EntityDetailsEndpoint {
    EndpointAcceptor1<EntityDetailsRequest> acceptor = EndpointAcceptor1.justOneThing("get", EntityDetailsRequest::new);

    static EndPoint entityDetailsEndPoint(Function<EntityDetailsRequest, CompletableFuture<EntityDetailsResponse>> fn) { return EndPoint.simple(acceptor, fn); }
}