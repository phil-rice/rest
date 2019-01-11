package one.xingyi.restAnnotations;
import one.xingyi.restAnnotations.annotations.XingYiField;
import one.xingyi.restAnnotations.optics.Getter;
import one.xingyi.restAnnotations.optics.Lens;
import one.xingyi.restAnnotations.optics.Setter;

import java.util.List;

public interface IXingYi {
    <T extends XingYiDomain> T parse(String s);
    <T extends XingYiDomain> Getter<T, String> stringGetter(String name);
    <T extends XingYiDomain> Setter<T, String> stringSetter(String name);

    <T1 extends XingYiDomain, T2 extends XingYiDomain> Getter<T1, T2> objectGetter(String name);
    <T1 extends XingYiDomain, T2 extends XingYiDomain> Setter<T1, T2> objectSetter(String name);

    <T1 extends XingYiDomain, T2 extends XingYiDomain> Getter<T1, List<T2>> listGetter(String name);
    <T1 extends XingYiDomain, T2 extends XingYiDomain> Setter<T1, List<T2>> listSetter(String name);

}
