package one.xingYi.restExample;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ClientTest extends AbstractClientTest {
    @Override protected Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient() { return EndPoint.toKliesli(composed); }
}
