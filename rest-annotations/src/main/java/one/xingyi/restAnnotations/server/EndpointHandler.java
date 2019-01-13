package one.xingyi.restAnnotations.server;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.http.Header;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class EndpointHandler implements HttpHandler {

    final Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> fn;

    @Override public void handle(HttpExchange exchange) {
        HttpUtils.process(exchange, () -> fn.apply(makeServiceRequest(exchange)).get());
    }

    ServiceRequest makeServiceRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod().toLowerCase();
        String body = Streams.readAll(exchange.getRequestBody());
        String uri = exchange.getRequestURI().toString();
        Headers responseHeaders = exchange.getResponseHeaders();
        List<Header> headers = new ArrayList<>();
        for (Map.Entry<String, List<String>> e : responseHeaders.entrySet()) {
            for (String v : e.getValue())
                headers.add(new Header(e.getKey(), v));
        }
        return new ServiceRequest(method, uri, headers, body);
    }


}
