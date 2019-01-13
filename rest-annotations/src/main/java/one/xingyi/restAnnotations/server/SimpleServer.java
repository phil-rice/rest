package one.xingyi.restAnnotations.server;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.utils.WrappedException;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

@ToString
@EqualsAndHashCode
//@RequiredArgsConstructor
public class SimpleServer {

    final HttpServer server;
    final Executor executor;
    final int port;


    public SimpleServer(Executor executor, HttpHandler handler, int port) {
        this.server = WrappedException.wrapCallable(() -> HttpServer.create());
        this.executor = executor;
        this.port = port;
        server.createContext("/", handler);
        server.setExecutor(executor);
    }

    public void start() {
        WrappedException.wrap(() -> server.bind(new InetSocketAddress(port), 0));
        server.start();
    }
    public void stop() {
        server.stop(1);
    }

}
