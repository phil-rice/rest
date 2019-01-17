package one.xingyi.restcore.xingyiclient;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.client.Client;
import one.xingyi.restAnnotations.clientside.*;
import one.xingyi.restAnnotations.entity.IOpsClientCompanion;
import one.xingyi.restAnnotations.http.Header;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.javascript.IXingYiFactory;
import one.xingyi.restcore.xingYiServer.IEntityUrlPattern;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
@ToString
@EqualsAndHashCode
public class SimpleClient implements Client {
    public static Client using(String hostAndPort, Function<ServiceRequest, CompletableFuture<ServiceResponse>> client, IClientFactory... factories) {
        return new SimpleClient(hostAndPort, client, factories);
    }
    public static Client using(String hostAndPort, Function<ServiceRequest, CompletableFuture<ServiceResponse>> client, List<IClientFactory> factories) {
        return new SimpleClient(hostAndPort, client, factories.toArray(new IClientFactory[0]));
    }

    final IXingYiFactory xingYiFactory = IXingYiFactory.xingYi;// not final so that we can test it.
    final IXingYiResponseSplitter splitter = IXingYiResponseSplitter.splitter; // not final so that we can test it.
    final String hostAndPort;
    final Function<ServiceRequest, CompletableFuture<ServiceResponse>> client;
    final IClientFactory[] factories;
    public final IClientFactory factory;

    @Override
    public <Interface extends IXingYiClientOps<?>, Result> CompletableFuture<Result> primitiveGet(Class<Interface> interfaceClass, String url, Function<Interface, Result> fn) {
        IOpsClientCompanion companion = factory.findCompanion().apply(interfaceClass).orElseThrow(runtimeExceptionSupplier(interfaceClass));
        ServiceRequest sr = new ServiceRequest("get", url, List.of(new Header("Accept", "application/xingyi.json.")), "");
        return client.apply(sr).thenApply(sRes -> fn.apply(processResult(interfaceClass, sRes)));
    }
    public SimpleClient(String hostAndPort, Function<ServiceRequest, CompletableFuture<ServiceResponse>> client, IClientFactory[] factories) {
        this.hostAndPort = hostAndPort;
        this.client = client;
        this.factories = factories;
        this.factory = IClientFactory.compose(factories);
    }
    @Override public <Interface extends IXingYiClientOps<?>> CompletableFuture<String> getUrlPattern(Class<Interface> interfaceClass) {
        IOpsClientCompanion companion = factory.findCompanion().apply(interfaceClass).orElseThrow(runtimeExceptionSupplier(interfaceClass));
        return this.<IEntityUrlPattern, String>primitiveGet(IEntityUrlPattern.class, hostAndPort + companion.entityCompanion().bookmark(), IEntityUrlPattern::url);

    }

    <Interface extends IXingYiClientOps<?>> Interface processResult(Class<Interface> interfaceClass, ServiceResponse serviceResponse) {
        DataAndJavaScript dataAndJavaScript = splitter.apply(serviceResponse);
        IXingYi xingYi = xingYiFactory.apply(dataAndJavaScript.javascript);
        Object mirror = xingYi.parse(dataAndJavaScript.data);
        Optional<Interface> opt = factory.apply(interfaceClass, xingYi, mirror);
        return opt.orElseThrow(runtimeExceptionSupplier(interfaceClass));
    }
    Supplier<RuntimeException> runtimeExceptionSupplier(Class<?> interfaceClass) {return () -> new RuntimeException("Cannot work out how to load " + interfaceClass + " Legal values are: " + factory.supported() + "\n\nIf this is a composite interface did you manually add the companion?\n");}
}
