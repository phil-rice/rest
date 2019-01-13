package one.xingyi.restcore.xingYiServer;


import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class EntityServer implements Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> {

    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return null;
    }
}
