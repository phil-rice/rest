package one.xingyi.restAnnotations.access;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
public interface IEntityRead<T> {
    CompletableFuture<T> read(String id);

    static <T> IEntityRead<T> map(Object lock, Map<String, T> map) { return new MapEntityRead<T>(lock, map); }
}


@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class MapEntityRead<T> implements IEntityRead<T> {
    final Object lock;
    final Map<String, T> map;

    @Override public CompletableFuture<T> read(String id) {
        synchronized (lock) {
            return CompletableFuture.completedFuture(map.get(id));
        }
    }
}

