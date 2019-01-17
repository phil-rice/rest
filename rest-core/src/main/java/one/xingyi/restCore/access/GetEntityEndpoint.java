package one.xingyi.restcore.access;

import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.endpoints.EndpointAcceptor1;
import one.xingyi.restAnnotations.entity.Companion;
import one.xingyi.restAnnotations.marshelling.ContextForJson;
import one.xingyi.restAnnotations.marshelling.HasJson;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.entity.EntityDetailsRequest;
import one.xingyi.restAnnotations.entity.EntityRegister;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface GetEntityEndpoint {

    static <E> EndpointAcceptor1<EntityDetailsRequest> acceptor(Companion<?, E> companion) {
        return EndpointAcceptor1.nameThenId("get", companion.entityName(), EntityDetailsRequest::new);
    }

    static <J, E extends HasJson<ContextForJson>> EndPoint getEntityEndpoint(JsonTC<J> jsonTC, EntityRegister entityRegister, Companion<?, E> companion, Function<String, CompletableFuture<E>> fn) {
        return EndPoint.javascriptAndJson(jsonTC, 200, acceptor(companion), ed -> fn.apply(ed.entityName), entityRegister.javascript());
    }
    static <J, E extends HasJson<ContextForJson>> EndPoint getOptionalEndPoint(JsonTC<J> jsonTC, EntityRegister entityRegister, Companion<?, E> companion, Function<String, CompletableFuture<Optional<E>>> fn) {
        return EndPoint.optionalJavascriptAndJson(jsonTC, 200, acceptor(companion), ed -> fn.apply(ed.entityName), entityRegister.javascript());
    }
}
