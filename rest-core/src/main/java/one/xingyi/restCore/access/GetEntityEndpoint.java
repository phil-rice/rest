package one.xingyi.restcore.access;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.endpoints.EndpointAcceptor1;
import one.xingyi.restAnnotations.endpoints.EndpointRequest;
import one.xingyi.restAnnotations.entity.Companion;
import one.xingyi.restAnnotations.marshelling.HasJson;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restcore.entity.EntityDetailsRequest;
import one.xingyi.restcore.xingYiServer.Entity;
import one.xingyi.restcore.xingYiServer.EntityServerCompanion;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public interface GetEntityEndpoint {

    static <E> EndpointAcceptor1<EntityDetailsRequest> acceptor(Companion<?, E> companion) {
        return EndpointAcceptor1.nameThenId("get", companion.entityName(), EntityDetailsRequest::new);
    }

    static <J, E extends HasJson> EndPoint getEntityEndpoint(JsonTC<J> jsonTC, Companion<?, E> companion, Function<String, CompletableFuture<E>> fn) {
        return EndPoint.javascriptAndJson(jsonTC, 200, acceptor(companion), ed -> fn.apply(ed.entityName), companion);
    }
    static <J, E extends HasJson> EndPoint getOptionalEndPoint(JsonTC<J> jsonTC, Companion<?, E> companion, Function<String, CompletableFuture<Optional<E>>> fn) {
        return EndPoint.optionalJavascriptAndJson(jsonTC, 200, acceptor(companion), ed -> fn.apply(ed.entityName), companion);
    }
}
