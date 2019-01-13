package one.xingyi.restAnnotations.clientside;

import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.server.EndpointHandler;
import one.xingyi.restAnnotations.server.HttpUtils;
import one.xingyi.restAnnotations.server.SimpleServer;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
public class JavaHttpClientTest {
    Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> endpoint =
            sr -> {
                System.out.println(sr.toString());
                return CompletableFuture.completedFuture(Optional.of(ServiceResponse.html(200, "made it: you sent" + sr)));
            };

    @Test
    public void testHitsSomething() throws ExecutionException, InterruptedException {
        ExecutorService executor = HttpUtils.makeDefaultExecutor();
        SimpleServer server = new SimpleServer(executor, new EndpointHandler(endpoint), 9000);
        server.start();
        try {
            CompletableFuture<ServiceResponse> future = JavaHttpClient.client.apply(new ServiceRequest("get", "http://localhost:9000/something", Arrays.asList(), ""));
            System.out.println(future.get());
        } finally {
            server.stop();
            executor.shutdownNow();
        }
    }
}
