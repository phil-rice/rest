package one.xingyi.restAnnotations.clientside;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ObjectifiedClient<Req, Res> implements Function<Req, CompletableFuture<Res>> {
    final Function<ServiceRequest, CompletableFuture<ServiceResponse>> service;
    final Function<Req, ServiceRequest> toServiceRequest;
    final Function<ServiceResponse, Res> toServiceResponse;

    @Override public CompletableFuture<Res> apply(Req req) {
        return service.apply(toServiceRequest.apply(req)).thenApply(toServiceResponse);
    }
}
