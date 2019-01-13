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
    <Interface> Optional<Interface> applyOnce(Class<Interface> clazz, IXingYi xingYi, Object mirror) {
        for (IClientFactory factory : factories) {
            Optional<Interface> opt = factory.apply(clazz, xingYi, mirror);
            if (opt.isPresent())
                return opt;
        }
        return Optional.empty();
    }
    @Override public <Interface> Optional<Interface> apply(Class<Interface> clazz, IXingYi xingYi, Object mirror) {
        return applyOnce(clazz, xingYi, mirror).or(() -> applyInterfaces(clazz, xingYi, mirror));
    }
    <Interface> Optional<Interface> applyInterfaces(Class<Interface> clazz, IXingYi xingYi, Object mirror) {
        for (IClientFactory factory : factories) {
            Optional<Interface> opt = checkFactoryForInterfaces(clazz, xingYi, mirror, factory);
            System.out.println("   opt afterchecking multiple: " + opt);
            if (opt.isPresent())
                return opt;
        }
        return Optional.empty();
    }

    <Interface> Optional<Interface> checkFactoryForInterfaces(Class<Interface> clazz, IXingYi xingYi, Object mirror, IClientFactory factory) {
        List<Class<?>> interfaces = Arrays.asList(clazz.getInterfaces());
        Interface iSaved = null;
        for (Class<?> i : interfaces) {
            Optional opt = factory.apply(i, xingYi, mirror);
            System.out.println("   opt: " + opt + "i " + i + " isaved" + iSaved + " supported" + factory.supported());
            if (!opt.isPresent())
                return Optional.empty();
            if (iSaved != null && opt.get().getClass() != iSaved.getClass()) //TODO this is very messy: making too many objects. Need to split up client factory must be same object
                return Optional.empty();
            iSaved = (Interface) opt.get();
        }
        return Optional.of(iSaved);
    }

}