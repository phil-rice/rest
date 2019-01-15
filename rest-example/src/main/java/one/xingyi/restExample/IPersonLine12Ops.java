package one.xingyi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiOps;

@XingYiOps
public interface IPersonLine12Ops extends IXingYiOps<IPerson> {
    String line1();
    String line2();
}
