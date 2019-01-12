package one.xingyi.restAnnotations;
import one.xingyi.restAnnotations.annotations.XingYiField;
import one.xingyi.restAnnotations.optics.Getter;
import one.xingyi.restAnnotations.optics.Lens;
import one.xingyi.restAnnotations.optics.Setter;

import java.util.List;

public interface IXingYi {
    <T extends XingYiDomain> T parse(String s);
    <T extends XingYiDomain> Lens<T, String> stringLens(String name);

    <T1 extends XingYiDomain, T2> Lens<T1, T2> objectLens(String name);

    <T1 extends XingYiDomain, T2> Lens<T1, List<T2>> listLens(String name);

}
