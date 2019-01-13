package one.xingyi.restAnnotations.javascript;
import one.xingyi.restAnnotations.annotations.XingYi;

import java.util.function.Function;
public interface IXingYiFactory extends Function<String, IXingYi> {
    IXingYi apply(String javascript);

    static IXingYiFactory xingYi = javascript -> new DefaultXingYi(javascript);
}
