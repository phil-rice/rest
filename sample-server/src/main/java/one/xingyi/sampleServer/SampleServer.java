package one.xingyi.sampleServer;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.endpoints.EndpointAcceptor0;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.server.EndpointHandler;
import one.xingyi.restAnnotations.server.HttpUtils;
import one.xingyi.restAnnotations.server.SimpleServer;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public class SampleServer {

    public static void main(String[] args) {
        EndPoint index = EndPoint.function(EndpointAcceptor0.exact("get", "/"), sr -> {
            System.out.println(sr);
            return ServiceResponse.html(200, "made it: you sent" + sr);
        });
        EndPoint keepalive = EndPoint.staticEndpoint(EndpointAcceptor0.exact("get", "/keepalive"), ServiceResponse.html(200, "Alive"));

        EndPoint all = EndPoint.printlnLog(EndPoint.compose(index, keepalive));
        SimpleServer server = new SimpleServer(HttpUtils.makeDefaultExecutor(), new EndpointHandler(all), 9000);
        server.start();
    }
}
