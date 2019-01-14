package one.xingyi.restAnnotations.entity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.marshelling.HasJson;
import one.xingyi.restAnnotations.marshelling.JsonTC;

import java.util.concurrent.CompletableFuture;
public interface Embedded<T> {
    /**
     * Do I already have it?
     */
    boolean have();
    /**
     * Go getEntity me the thing
     */
    CompletableFuture<T> get();

    static <T > Embedded value(T t) {return new ActuallyEmbedded<T>(t);}
}

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class ActuallyEmbedded<T> implements Embedded<T> {
    final T entity;

    @Override public boolean have() {
        return true;
    }
    @Override public CompletableFuture<T> get() {
        return CompletableFuture.completedFuture(entity);
    }
}
