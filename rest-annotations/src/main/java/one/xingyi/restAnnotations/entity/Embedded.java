package one.xingyi.restAnnotations.entity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.marshelling.HasJson;
import one.xingyi.restAnnotations.marshelling.JsonTC;

import java.util.concurrent.CompletableFuture;
public interface Embedded<T> extends HasJson {
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
class ActuallyEmbedded<T extends HasJson> implements Embedded<T> {
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
    @Override public <J> J toJson(JsonTC<J> jsonTc) {
        return jsonTc.makeObject("_embedded", entity.toJson(jsonTc));
    }
}
