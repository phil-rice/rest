package one.xingyi.restAnnotations.clientside;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.OptionalUtils;

import java.util.*;
import java.util.function.Function;

public interface IClientFactory extends IClientMaker {


    Set<Class<? extends IXingYiClientOps<?>>>  supported();

    Function<Class<? extends IXingYiClientOps<?>>, Optional<IClientCompanion>> findCompanion();

    default <Interface extends IXingYiClientOps<?>> Optional<Interface> apply(Class<Interface> clazz, IXingYi xingYi, Object mirror) { return findCompanion().apply(clazz).flatMap(c -> c.apply(clazz, xingYi, mirror)); }

    static IClientFactory compose(IClientFactory... factories) { return new ComposeClientFactory(Arrays.asList(factories)); }
}


@ToString
@EqualsAndHashCode
class ComposeClientFactory implements IClientFactory {
    final List<IClientFactory> factories;
    private final List<Function<Class<? extends IXingYiClientOps<?>>, Optional<IClientCompanion>>> findCompanions;
    public ComposeClientFactory(List<IClientFactory> factories) {
        this.factories = factories;
        this.findCompanions = ListUtils.map(factories, IClientFactory::findCompanion);
    }
    @Override public Set<Class<? extends IXingYiClientOps<?>>>  supported() {
        return ListUtils.aggLeft(new HashSet<Class<? extends IXingYiClientOps<?>>>(), factories, (acc, f) -> acc.addAll(f.supported()));
    }
    @Override public Function<Class<? extends IXingYiClientOps<?>>, Optional<IClientCompanion>> findCompanion() { return OptionalUtils.chainFn(findCompanions); }

    @Override public <Interface extends IXingYiClientOps<?>> Optional<Interface> apply(Class<Interface> clazz, IXingYi xingYi, Object mirror) {
        return findCompanion().apply(clazz).flatMap(c -> c.apply(clazz, xingYi, mirror)).
                or(() -> OptionalUtils.allReturnSameTo(findCompanions).apply(clazz).flatMap(c -> c.apply(clazz, xingYi, mirror)));
    }
}