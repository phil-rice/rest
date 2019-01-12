package one.xingyi.restAnnotations.javascript;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import java.util.function.Function;

import static one.xingyi.restAnnotations.utils.WrappedException.wrap;
public class IXingYiLoader implements Function<String, IXingYi> {
    @Override public IXingYi apply(String javaScript) {
        return XingYiExecutionException.wrap("Initialising", () -> {
            ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine("--language=es6 ");
            engine.eval(javaScript);
            return new DefaultXingYi(engine);
        });
    }
}
