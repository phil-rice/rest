package one.xingyi.restAnnotations.entity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.marshelling.ContextForJson;
import one.xingyi.restAnnotations.marshelling.HasJson;
import one.xingyi.restAnnotations.marshelling.JsonTC;

import java.util.concurrent.CompletableFuture;
public interface EmbeddedWithHasJson<T> extends Embedded, HasJson<ContextForJson> {

    static <T extends HasJson> EmbeddedWithHasJson value(T t) {return new ActuallyEmbeddedWithJson(t);}
}

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class ActuallyEmbeddedWithJson<T extends HasJson<ContextForJson>> implements EmbeddedWithHasJson<T> {
    final T entity;

    @Override public boolean have() {
        return true;
    }
    @Override public CompletableFuture<T> get() {
        return CompletableFuture.completedFuture(entity);
    }
    @Override public <J> J toJson(JsonTC<J> jsonTc, ContextForJson contextForJson) { return jsonTc.makeObject("_embedded", entity.toJson(jsonTc, contextForJson)); }
}
