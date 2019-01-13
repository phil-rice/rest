package one.xingyi.restAnnotations.entity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.concurrent.CompletableFuture;
public interface Embedded<T> {
    /**
     * If you are an embedded object you need an id
     */
    String id();

    /**
     * Do I already have it?
     */
    boolean have();
    /**
     * Go get me the thing
     */
    CompletableFuture<T> get();
}

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class ActuallyEmbedded<T> implements Embedded<T> {
    final String id;
    final T entity;

    @Override public String id() {
        return id;
    }
    @Override public boolean have() {
        return true;
    }
    @Override public CompletableFuture<T> get() {
        return CompletableFuture.completedFuture(entity);
    }
}
