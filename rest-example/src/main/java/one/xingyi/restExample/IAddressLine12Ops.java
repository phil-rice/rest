package one.xingyi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiOps;

@XingYiOps
public interface IAddressLine12Ops extends IXingYiOps<IAddress> {
    String line1();

    String line2();

}
