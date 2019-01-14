package one.xingYi.restExample;
import one.xingyi.restAnnotations.clientside.JavaHttpClient;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.server.EndpointHandler;
import one.xingyi.restAnnotations.server.SimpleServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
public class ProperClientTest extends AbstractClientTest {

    private static SimpleServer server;
    private static ExecutorService executorService;

    @AfterClass
    public static void killServer() {
        server.stop();
        executorService.shutdownNow();
    }

    @Override protected Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient() {
        if (server == null) {
            executorService = Executors.newFixedThreadPool(20);
            server = new SimpleServer(executorService, new EndpointHandler(composed), 9000);
            server.start();
        }
        return JavaHttpClient.client;
    }
}
