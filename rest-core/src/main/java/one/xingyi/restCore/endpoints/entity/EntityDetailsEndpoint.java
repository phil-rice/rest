package one.xingyi.restcore.endpoints.entity;


import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restcore.endpoints.EndPoint;
import one.xingyi.restcore.endpoints.EndpointAcceptor1;
import one.xingyi.restcore.xingYiServer.Entity;
import one.xingyi.restcore.xingYiServer.EntityServerCompanion;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface EntityDetailsEndpoint {
    EndpointAcceptor1<EntityDetailsRequest> acceptor = EndpointAcceptor1.justOneThing("get", EntityDetailsRequest::new);

    EntityServerCompanion companion = new EntityServerCompanion();

    static <J> EndPoint entityDetailsEndPoint(JsonTC<J> jsonTC, Function<EntityDetailsRequest, CompletableFuture<Entity>> fn) {
        return EndPoint.javascriptAndJson(jsonTC, 200, acceptor, fn, companion);
    }
}