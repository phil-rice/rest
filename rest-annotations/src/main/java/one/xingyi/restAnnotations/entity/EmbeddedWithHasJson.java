package one.xingyi.restAnnotations.entity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.marshelling.ContextForJson;
import one.xingyi.restAnnotations.marshelling.HasJson;
import one.xingyi.restAnnotations.marshelling.JsonTC;

import java.util.concurrent.CompletableFuture;

public interface EmbeddedWithHasJson<T> extends Embedded, HasJson<ContextForJson> {

    //TODO I don't like this typecasting, but it's currently only used in tests to create data
    @SuppressWarnings("unchecked")
    static <T> EmbeddedWithHasJson<T> valueForTest(T t) {return new ActuallyEmbeddedWithJson((HasJson<ContextForJson>) t);}
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
