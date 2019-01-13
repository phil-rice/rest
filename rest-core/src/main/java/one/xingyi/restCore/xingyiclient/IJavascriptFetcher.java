package one.xingyi.restcore.xingyiclient;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface IJavascriptFetcher extends Function<String, CompletableFuture<String>> {

}
