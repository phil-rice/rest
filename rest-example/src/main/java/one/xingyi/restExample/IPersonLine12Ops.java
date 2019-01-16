package one.xingyi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiServerOps;

@XingYiOps
public interface IPersonLine12Ops extends IXingYiServerOps<IPerson> {
    String line1();
    String line2();
}
