package one.xingyi.restcore.xingyiclient;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.clientside.*;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.javascript.IXingYiFactory;
import one.xingyi.restcore.xingYiServer.IEntityUrlPattern;

import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
public interface XingYiClient {


    <Interface extends IXingYiOps<?>, Result> CompletableFuture<Result> primitiveGet(Class<Interface> interfaceClass, String url, Function<Interface, Result> fn);

    <Interface> CompletableFuture<String> getUrlPattern(Class<Interface> interfaceClass);

    default <Interface extends IXingYiOps<?>, Result> CompletableFuture<Result> get(Class<Interface> interfaceClass, String id, Function<Interface, Result> fn) {
        return getUrlPattern(interfaceClass).thenCompose(url -> primitiveGet(interfaceClass, url.replace("<id>", URLEncoder.encode(id)), fn));
    }
    static XingYiClient using(String hostAndPort, Function<ServiceRequest, CompletableFuture<ServiceResponse>> client, IClientFactory... factories) {
        return new SimpleXingYiClient(hostAndPort, client, factories);
    }
}


@ToString
@EqualsAndHashCode
class SimpleXingYiClient implements XingYiClient {
    final IXingYiFactory xingYiFactory = IXingYiFactory.xingYi;// not final so that we can test it.
    final IXingYiResponseSplitter splitter = IXingYiResponseSplitter.splitter; // not final so that we can test it.
    final String hostAndPort;
    final Function<ServiceRequest, CompletableFuture<ServiceResponse>> client;
    final IClientFactory[] factories;
    final IClientFactory factory;

    public SimpleXingYiClient(String hostAndPort, Function<ServiceRequest, CompletableFuture<ServiceResponse>> client, IClientFactory[] factories) {
        this.hostAndPort = hostAndPort;
        this.client = client;
        this.factories = factories;
        this.factory = IClientFactory.compose(factories);
    }
    @Override
    public <Interface extends IXingYiOps<?>, Result> CompletableFuture<Result> primitiveGet(Class<Interface> interfaceClass, String url, Function<Interface, Result> fn) {
        ServiceRequest sr = new ServiceRequest("getEntity", url, List.of(), "");
        return client.apply(sr).thenApply(sRes -> fn.apply(processResult(interfaceClass, sRes)));
    }
    @Override public <Interface> CompletableFuture<String> getUrlPattern(Class<Interface> interfaceClass) {
        IClientMaker companion = factory.findCompanion().apply(interfaceClass).orElseThrow(runtimeExceptionSupplier(interfaceClass));
        if (companion instanceof IClientCompanion)
            return primitiveGet(IEntityUrlPattern.class, hostAndPort + ((IClientCompanion) companion).bookmark(), IEntityUrlPattern::url);
        else throw new IllegalStateException("Cannot call this method with interface " + interfaceClass.getName() + " because there is no 'companion' object for it" );
    }

    <Interface> Interface processResult(Class<Interface> interfaceClass, ServiceResponse serviceResponse) {
        DataAndJavaScript dataAndJavaScript = splitter.apply(serviceResponse);
        IXingYi xingYi = xingYiFactory.apply(dataAndJavaScript.javascript);
        Object mirror = xingYi.parse(dataAndJavaScript.data);
        Optional<Interface> opt = factory.apply(interfaceClass, xingYi, mirror);
        return opt.orElseThrow(runtimeExceptionSupplier(interfaceClass));
    }
    Supplier<RuntimeException> runtimeExceptionSupplier(Class<?> interfaceClass) {return () -> new RuntimeException("Cannot work out how to load " + interfaceClass + " Legal values are: " + factory.supported());}
}
