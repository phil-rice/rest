package one.xingyi.restAnnotations.server;
import com.sun.net.httpserver.HttpExchange;
import lombok.val;
import one.xingyi.restAnnotations.http.Header;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.utils.WrappedException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class HttpUtils {

    public static ExecutorService makeDefaultExecutor() {return Executors.newFixedThreadPool(100);}

    public static void write(HttpExchange exchange, ServiceResponse response) throws IOException {
        for (Header h : response.headers)
            exchange.getResponseHeaders().set(h.name, h.value);
        //    exchange.getResponseHeaders.set("content-type", response.contentType.fold("text/plain")(_.value))
        val bytes = response.body.getBytes("UTF-8");
        exchange.sendResponseHeaders(response.statusCode, bytes.length);
        Streams.sendAll(exchange.getResponseBody(), bytes);
    }

    public static void process(HttpExchange exchange, Callable<Optional<ServiceResponse>> response) {
        try {
            Optional<ServiceResponse> result = response.call();
            result.ifPresentOrElse(WrappedException.<ServiceResponse>wrapConsumer(x -> write(exchange, x)),
                    () -> WrappedException.wrap(() -> write(exchange, ServiceResponse.html(404, "Not found. " + exchange.getRequestURI()))));
        } catch (Exception e) {
            WrappedException.wrap(() -> {
                        ServiceResponse serviceResponse = ServiceResponse.html(500, e.getClass().getName() + "\n" + e.getMessage());
                        write(exchange, serviceResponse);
                    }
            );
        }
    }


//    def id(httpExchange:HttpExchange, prefix:String):
//    String =httpExchange.getRequestURI.getPath.substring(prefix.length +1)

}
