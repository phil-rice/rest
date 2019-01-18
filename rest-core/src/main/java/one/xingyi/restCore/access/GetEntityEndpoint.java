package one.xingyi.restcore.access;

import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.endpoints.EndpointAcceptor1;
import one.xingyi.restAnnotations.entity.Companion;
import one.xingyi.restAnnotations.javascript.JavascriptStore;
import one.xingyi.restAnnotations.marshelling.ContextForJson;
import one.xingyi.restAnnotations.marshelling.HasJson;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.entity.EntityDetailsRequest;
import one.xingyi.restAnnotations.entity.EntityRegister;
import one.xingyi.restAnnotations.utils.Digestor;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface GetEntityEndpoint {

    static <E> EndpointAcceptor1<EntityDetailsRequest> acceptor(Companion<?, E> companion) {
        return EndpointAcceptor1.nameThenId("get", companion.entityName(), EntityDetailsRequest::new);
    }

    static <J, E extends HasJson<ContextForJson>> EndPoint getEntityEndpoint
            (JsonTC<J> jsonTC, EntityRegister entityRegister, Companion<?, E> companion, Function<String, CompletableFuture<E>> fn) {
        JavascriptStore javascriptStore = JavascriptStore.finder(Digestor.sha256, entityRegister);
        return EndPoint.javascriptAndJson(jsonTC, 200, acceptor(companion), ed -> fn.apply(ed.entityName), javascriptStore);
    }
    static <J, E extends HasJson<ContextForJson>> EndPoint getOptionalEndPoint
            (JsonTC<J> jsonTC, EntityRegister entityRegister, Companion<?, E> companion, Function<String, CompletableFuture<Optional<E>>> fn) {
        JavascriptStore javascriptStore = JavascriptStore.finder(Digestor.sha256, entityRegister);
        return EndPoint.optionalJavascriptAndJson(jsonTC, 200, acceptor(companion), ed -> fn.apply(ed.entityName), javascriptStore);
    }
}
