package one.xingyi.restAnnotations.javascript;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import one.xingyi.restAnnotations.optics.Getter;
import one.xingyi.restAnnotations.optics.Lens;
import one.xingyi.restAnnotations.optics.Setter;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.concurrent.Callable;

public interface IXingYi {
    Object parse(String s);
    <T extends XingYiDomain> Lens<T, String> stringLens(IDomainMaker<T> domainMaker, String name);
    <T1 extends XingYiDomain, T2> Lens<T1, T2> objectLens(IDomainMaker<T1> domainMaker1, IDomainMaker<T2> domainMaker2, String name);
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

    DefaultXingYi(String javaScript) {
        long time = System.nanoTime();
        engine = new NashornScriptEngineFactory().getScriptEngine("--language=es6 ");
        XingYiExecutionException.wrap("initialising", () -> engine.eval(javaScript));
        this.inv = (Invocable) engine;
        System.out.println("Duration: " + (System.nanoTime() - time)/1000000);
    }

    @Override public Object parse(String s) { return XingYiExecutionException.wrap("parse", () -> inv.invokeFunction("parse", s)); }
    @Override public <T extends XingYiDomain> Lens<T, String> stringLens(IDomainMaker<T> domainMaker, String name) {
        Getter<T, String> getter = t -> XingYiExecutionException.wrap("stringLens.get" + name, () -> (String) inv.invokeFunction("getL", name, t.mirror));
        Setter<T, String> setter = (t, s) -> XingYiExecutionException.wrap("stringLens.set" + name, () -> domainMaker.apply(inv.invokeFunction("setL", name, t.mirror, s), this));
        return Lens.create(getter, setter);
    }
    @Override public <T1 extends XingYiDomain, T2> Lens<T1, T2> objectLens(IDomainMaker<T1> domainMaker1, IDomainMaker<T2> domainMaker2, String name) {
        Getter<T1, T2> getter = t -> XingYiExecutionException.<T2>wrap("objectLens.get" + name, () -> domainMaker2.apply(inv.invokeFunction("getL", name, t.mirror), this));
        Setter<T1, T2> setter = (t, s) -> XingYiExecutionException.<T1>wrap("objectLens.set" + name, () -> domainMaker1.apply(inv.invokeFunction("setL", name, t.mirror, s), this));
        return Lens.create(getter, setter);
    }
}
