package one.xingyi.restAnnotations.clientside;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.http.Header;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
 class SimpleHttpClient<HttpReq, HttpRes> implements Function<ServiceRequest, CompletableFuture<ServiceResponse>> {
    final Function<HttpReq, CompletableFuture<HttpRes>> service;
    final Function<ServiceRequest, HttpReq> toServiceRequest;
    final Function<HttpRes, ServiceResponse> toServiceResponse;

    @Override public CompletableFuture<ServiceResponse> apply(ServiceRequest serviceRequest) {
        return service.apply(toServiceRequest.apply(serviceRequest)).thenApply(toServiceResponse);
    }
}

