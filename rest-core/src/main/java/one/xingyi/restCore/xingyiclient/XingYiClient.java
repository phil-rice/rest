package one.xingyi.restcore.xingyiclient;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.clientside.DataAndJavaScript;
import one.xingyi.restAnnotations.clientside.IClientFactory;
import one.xingyi.restAnnotations.clientside.IXingYiResponseSplitter;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.javascript.IXingYiFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface XingYiClient {

    static XingYiClient using(Function<ServiceRequest, CompletableFuture<ServiceResponse>> client, IClientFactory... factories) {
        return new SimpleXingYiClient(client, factories);
    }

    <Interface, Result> CompletableFuture<Result> getFromUrl(Class<Interface> interfaceClass, String url, Function<Interface, Result> fn);
}


@ToString
@EqualsAndHashCode
class SimpleXingYiClient implements XingYiClient {
    final IXingYiFactory xingYiFactory = IXingYiFactory.xingYi;// not final so that we can test it.
    final IXingYiResponseSplitter splitter = IXingYiResponseSplitter.splitter; // not final so that we can test it.
    final Function<ServiceRequest, CompletableFuture<ServiceResponse>> client;
    final IClientFactory[] factories;
    private final IClientFactory factory;
    public SimpleXingYiClient(Function<ServiceRequest, CompletableFuture<ServiceResponse>> client, IClientFactory[] factories) {
        this.client = client;
        this.factories = factories;
        this.factory = IClientFactory.compose(factories);
    }
    @Override
    public <Interface, Result> CompletableFuture<Result> getFromUrl(Class<Interface> interfaceClass, String url, Function<Interface, Result> fn) {
        ServiceRequest sr = new ServiceRequest("get", url, List.of(), "");
        return client.apply(sr).thenApply(sRes -> fn.apply(processResult(interfaceClass, sRes)));
    }

    <Interface> Interface processResult(Class<Interface> interfaceClass, ServiceResponse serviceResponse) {
        DataAndJavaScript dataAndJavaScript = splitter.apply(serviceResponse);
        IXingYi xingYi = xingYiFactory.apply(dataAndJavaScript.javascript);
        Object mirror = xingYi.parse(dataAndJavaScript.data);
        Optional<Interface> opt = factory.apply(interfaceClass, xingYi, mirror);
        return opt.orElseThrow(() -> new RuntimeException("Cannot work out how to load " + interfaceClass + " Legal values are: " + factory.supported()));
    }
}
