package one.xingyi.restAnnotations.endpoints;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.entity.Companion;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.marshelling.HasJson;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.utils.Files;
import one.xingyi.restAnnotations.utils.OptionalUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface EndPoint extends Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> {

    static Function<ServiceRequest, CompletableFuture<ServiceResponse>> toKliesli(Function<ServiceRequest, CompletableFuture<Optional<ServiceResponse>>> original) {
        return sr -> {
            try {
                return original.apply(sr).thenApply(opt -> opt.orElse(ServiceResponse.html(404, "Not found. " + sr))).exceptionally(e -> internalError(e));
            } catch (Exception e) {
                System.out.println("Dumping error from inside completable future in toKliesli");
                e.printStackTrace();
                return CompletableFuture.completedFuture(internalError(e));
            }
        };
    }
    static ServiceResponse internalError(Throwable e) {
        return ServiceResponse.html(500, e.getClass().getName() + "\n" + e.getMessage());
    }


    // try {
    //            Optional<ServiceResponse> result = response.call();
    //            result.ifPresentOrElse(WrappedException.<ServiceResponse>wrapConsumer(x -> write(exchange, x)),
    //                    () -> WrappedException.wrap(() -> write(exchange, ServiceResponse.html(404, "Not found. " + exchange.getRequestURI()))));
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            WrappedException.wrap(() -> {
    //                        ServiceResponse serviceResponse = ServiceResponse.html(500, e.getClass().getName() + "\n" + e.getMessage());
    //                        write(exchange, serviceResponse);
    //                    }
    //            );
    //        }

    static <From extends EndpointRequest, To extends EndpointResponse> EndPoint simple(EndpointAcceptor1<From> acceptor, Function<From, CompletableFuture<To>> fn) {
        return new SimpleEndPoint<>(acceptor, fn);
    }
    static <J, From extends EndpointRequest, To extends HasJson> EndPoint json(JsonTC<J> jsonTC, int status, EndpointAcceptor1<From> acceptor, Function<From, CompletableFuture<To>> fn) {
        return new JsonEndPoint<>(jsonTC, status, acceptor, fn);
    }
    static <J, From extends EndpointRequest, Interface, To extends HasJson> EndPoint javascriptAndJson(JsonTC<J> jsonTC, int status, EndpointAcceptor1<From> acceptor, Function<From, CompletableFuture<To>> fn, Companion<Interface, To> companion) {
        return new JavascriptAndJsonEndPoint<>(jsonTC, status, acceptor, fn, companion);
    }


    static EndPoint compose(EndPoint... endPoints) {return new ComposeEndPoints(Arrays.asList(endPoints));}
    static EndPoint staticEndpoint(EndpointAcceptor0 acceptor, ServiceResponse serviceResponse) {
        return sr -> CompletableFuture.completedFuture(OptionalUtils.from(acceptor.apply(sr), () -> serviceResponse));
    }
    static EndPoint function(EndpointAcceptor0 acceptor, Function<ServiceRequest, ServiceResponse> fn) {
        return sr -> CompletableFuture.completedFuture(OptionalUtils.from(acceptor.apply(sr), () -> fn.apply(sr)));
    }
    static EndPoint printlnLog(EndPoint endPoint) {
        return sr -> endPoint.apply(sr).thenApply(res -> {
            System.out.println(sr);
            System.out.println(res);
            System.out.println();
            return res;
        });

    }
}


@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class ComposeEndPoints implements EndPoint {
    final List<EndPoint> endpoints;

    CompletableFuture<Optional<ServiceResponse>> recurse(ServiceRequest serviceRequest, int index) {
        if (index >= endpoints.size())
            return CompletableFuture.completedFuture(Optional.empty());
        EndPoint endPoint = endpoints.get(index);
        return endPoint.apply(serviceRequest).thenCompose(op -> {
            if (op.isEmpty())
                return recurse(serviceRequest, index + 1);
            else
                return CompletableFuture.completedFuture(op);
        });

    }

    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return recurse(serviceRequest, 0);
    }
}

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class SimpleEndPoint<From extends EndpointRequest, To extends EndpointResponse> implements EndPoint {

    final EndpointAcceptor1<From> acceptor;
    final Function<From, CompletableFuture<To>> fn;

    //wow this is a bit of dogs dinner
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return acceptor.apply(serviceRequest).
                map(from -> fn.apply(from).thenApply(to -> Optional.of(to.serviceResponse()))).
                orElse(CompletableFuture.completedFuture(Optional.empty()));
    }
}
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class JsonEndPoint<From extends EndpointRequest, To extends HasJson> implements EndPoint {

    final JsonTC<? extends Object> jsonTc;
    final int status;
    final EndpointAcceptor1<From> acceptor;
    final Function<From, CompletableFuture<To>> fn;

    //wow this is a bit of dogs dinner
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return OptionalUtils.flip(acceptor.apply(serviceRequest).map(fn)).thenApply(x -> x.map(to -> ServiceResponse.json(jsonTc, status, to)));
    }
}
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class JavascriptAndJsonEndPoint<From extends EndpointRequest, Interface, To extends HasJson> implements EndPoint {

    final JsonTC<? extends Object> jsonTc;
    final int status;
    final EndpointAcceptor1<From> acceptor;
    final Function<From, CompletableFuture<To>> fn;
    final Companion<Interface, To> companion;

    //wow this is a bit of dogs dinner
    @Override public CompletableFuture<Optional<ServiceResponse>> apply(ServiceRequest serviceRequest) {
        return OptionalUtils.flip(acceptor.apply(serviceRequest).map(fn)).thenApply(x -> x.map(to -> {
            String javascript = Files.getText("header.js") + companion.javascript();
            return ServiceResponse.javascriptAndJson(jsonTc, 200, to, javascript);
        }));
    }
}