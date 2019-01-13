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
public class XingYiHttpClient<HttpReq, HttpRes> implements Function<ServiceRequest, CompletableFuture<ServiceResponse>> {
    final Function<HttpReq, CompletableFuture<HttpRes>> service;
    final Function<ServiceRequest, HttpReq> toServiceRequest;
    final Function<HttpRes, ServiceResponse> toServiceResponse;

    @Override public CompletableFuture<ServiceResponse> apply(ServiceRequest serviceRequest) {
        return service.apply(toServiceRequest.apply(serviceRequest)).thenApply(toServiceResponse);
    }
}

interface JavaHttpClient {

    Function<HttpRequest, CompletableFuture<HttpResponse<String>>> service =
            req -> HttpClient.newHttpClient().sendAsync(req, HttpResponse.BodyHandlers.ofString());

    Function<ServiceRequest, HttpRequest> toJavaHttp = sr -> {
        URI uri = URI.create(sr.url);
        HttpRequest.Builder b1 = HttpRequest.newBuilder().
                method(sr.method, HttpRequest.BodyPublishers.ofString(sr.body)).
                uri(uri);

        HttpRequest.Builder b2 = ListUtils.foldLeft(b1, sr.headers, (b, h) -> b.header(h.name, h.value));
        return b2.build();
    };

    Function<HttpResponse<String>, ServiceResponse> toServiceResponse = hr -> {
        List<Header> headers = new ArrayList<>();
        for (Map.Entry<String, List<String>> e : hr.headers().map().entrySet()) {
            for (String value : e.getValue())
                headers.add(new Header(e.getKey(), value));
        }
        return new ServiceResponse(hr.statusCode(), hr.body(), headers);
    };

    Function<ServiceRequest, CompletableFuture<ServiceResponse>> client = new XingYiHttpClient<>(service, toJavaHttp, toServiceResponse);

}
