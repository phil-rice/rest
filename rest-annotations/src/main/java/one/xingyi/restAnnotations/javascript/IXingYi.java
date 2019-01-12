package one.xingyi.restAnnotations.javascript;
import one.xingyi.restAnnotations.optics.Getter;
import one.xingyi.restAnnotations.optics.Lens;
import one.xingyi.restAnnotations.optics.Setter;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.List;
import java.util.concurrent.Callable;

public interface IXingYi {
    <T extends XingYiDomain> T parse(Class<T> clazz, String s);
    <T extends XingYiDomain> Lens<T, String> stringLens(String name);
    <T1 extends XingYiDomain, T2> Lens<T1, T2> objectLens(String name);
}

class XingYiExecutionException extends RuntimeException {
    XingYiExecutionException(String s, Exception e) {super(s, e);}

    static <T> T wrap(String message, Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new XingYiExecutionException("Error executing " + message, e);
        }
    }
}

class DefaultXingYi implements IXingYi {

    final ScriptEngine engine;
    final Invocable inv;

    DefaultXingYi(ScriptEngine engine) {
        this.engine = engine;
        this.inv = (Invocable) engine;
    }

    @Override public <T extends XingYiDomain> T parse(Class<T> clazz, String s) {
        return XingYiExecutionException.wrap("parse", () -> {
            Object mirror = inv.invokeFunction("parse", s);
            return clazz.<T>getConstructor(Object.class, IXingYi.class).newInstance(mirror, this);
        });
    }
    @Override public <T extends XingYiDomain> Lens<T, String> stringLens(String name) {
        Getter<T, String> getter = t -> XingYiExecutionException.wrap("stringLens.get" + name, () -> (String) inv.invokeFunction("getL", name, t.mirror));
        Setter<T, String> setter = (t, s) -> XingYiExecutionException.wrap("stringLens.set" + name, () -> (T) inv.invokeFunction("setL", name, t.mirror, s));
        return Lens.create(getter, setter);
    }

    @Override public <T1 extends XingYiDomain, T2> Lens<T1, T2> objectLens(String name) {
        Getter<T1, T2> getter = t -> XingYiExecutionException.wrap("objectLens.get" + name, () -> (T2) inv.invokeFunction("getL", name, t.mirror));
        Setter<T1, T2> setter = (t, s) -> XingYiExecutionException.wrap("objectLens.set" + name, () -> (T1) inv.invokeFunction("setL", name, t.mirror, s));
        return Lens.create(getter, setter);
    }
}
