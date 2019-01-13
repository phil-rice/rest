package one.xingyi.restcore.endpoints.entity;


import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restcore.endpoints.EndPoint;
import one.xingyi.restcore.endpoints.EndpointAcceptor1;
import one.xingyi.restcore.xingYiServer.Entity;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface EntityDetailsEndpoint {
    EndpointAcceptor1<EntityDetailsRequest> acceptor = EndpointAcceptor1.justOneThing("get", EntityDetailsRequest::new);

    static <J> EndPoint entityDetailsEndPoint(JsonTC<J> jsonTC, Function<EntityDetailsRequest, CompletableFuture<Entity>> fn) {
        return EndPoint.json(jsonTC, 200, acceptor, fn);
    }
}