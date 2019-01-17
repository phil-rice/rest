package one.xingyi.restAnnotations.clientside;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.entity.IOpsClientCompanion;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.javascript.XingYiDomain;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.OptionalUtils;

import java.util.*;
import java.util.function.Function;

public interface IClientFactory extends IClientMaker {

    Set<Class<? extends IXingYiClientOps<?>>> supported();

    Function<Class<? extends IXingYiClientOps<?>>, Optional<IOpsClientCompanion>> findCompanion();

    default <Interface extends IXingYiClientOps<?>> Optional<Interface> apply(Class<Interface> clazz, IXingYi xingYi, Object mirror) {
        Function<Class<? extends IXingYiClientOps<?>>, Optional<IOpsClientCompanion>> companion = findCompanion();
        return companion.apply(clazz).map(c -> (Interface) c.makeImplementation(xingYi, mirror));//TODO It would be really nice to get rid of this typeclass
    }

    static IClientFactory compose(IClientFactory... factories) { return new ComposeClientFactory(Arrays.asList(factories)); }
}


@ToString
@EqualsAndHashCode
class ComposeClientFactory implements IClientFactory {
    final List<IClientFactory> factories;
    private final List<Function<Class<? extends IXingYiClientOps<?>>, Optional<IOpsClientCompanion>>> findCompanions;
    public ComposeClientFactory(List<IClientFactory> factories) {
        this.factories = factories;
        this.findCompanions = ListUtils.map(factories, IClientFactory::findCompanion);
    }
    @Override public Set<Class<? extends IXingYiClientOps<?>>> supported() {
        return ListUtils.aggLeft(new HashSet<Class<? extends IXingYiClientOps<?>>>(), factories, (acc, f) -> acc.addAll(f.supported()));
    }
    @Override public Function<Class<? extends IXingYiClientOps<?>>, Optional<IOpsClientCompanion>> findCompanion() { return OptionalUtils.chainFn(findCompanions); }

    @Override public <Interface extends IXingYiClientOps<?>> Optional<Interface> apply(Class<Interface> clazz, IXingYi xingYi, Object mirror) {
        return findCompanion().apply(clazz).map(c1 ->(Interface) c1.makeImplementation(xingYi, mirror)).
                or(() -> OptionalUtils.allReturnSameTo(findCompanions).apply(clazz).map(c -> (Interface)c.makeImplementation(xingYi, mirror)));
    }
}