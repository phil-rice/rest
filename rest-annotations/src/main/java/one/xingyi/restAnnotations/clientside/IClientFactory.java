package one.xingyi.restAnnotations.clientside;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.util.*;
public interface IClientFactory {
    Set<Class<?>> supported();
    <Interface> Optional<Interface> apply(Class<Interface> clazz, IXingYi xingYi, Object mirror);

    static IClientFactory compose(IClientFactory... factories) { return new ComposeClientFactory(Arrays.asList(factories)); }
}

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
class ComposeClientFactory implements IClientFactory {
    final List<IClientFactory> factories;

    @Override public Set<Class<?>> supported() {
        return ListUtils.foldLeft(new HashSet<Class<?>>(), factories, (acc, f) -> {
            acc.addAll(f.supported()); return acc;
        });
    }
    @Override public <Interface> Optional<Interface> apply(Class<Interface> clazz, IXingYi xingYi, Object mirror) {
        for (IClientFactory factory : factories) {
            Optional<Interface> opt = factory.apply(clazz, xingYi, mirror);
            if (opt.isPresent())
                return opt;
        }
        return Optional.empty();
    }
}
