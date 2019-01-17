package one.xingyi.restAnnotations.client;
import one.xingyi.restAnnotations.clientside.IXingYiClientOps;

import java.net.URLEncoder;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface Client {


    <Interface extends IXingYiClientOps<?>, Result> CompletableFuture<Result> primitiveGet(Class<Interface> interfaceClass, String url, Function<Interface, Result> fn);

    <Interface extends IXingYiClientOps<?>> CompletableFuture<String> getUrlPattern(Class<Interface> interfaceClass);

    default <Interface extends IXingYiClientOps<?>, Result> CompletableFuture<Result> get(Class<Interface> interfaceClass, String id, Function<Interface, Result> fn) {
        return getUrlPattern(interfaceClass).thenCompose(url -> primitiveGet(interfaceClass, url.replace("<id>", URLEncoder.encode(id)), fn));
    }
}



