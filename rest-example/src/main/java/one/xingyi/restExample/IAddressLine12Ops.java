package one.xingyi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiServerOps;

@XingYiOps
public interface IAddressLine12Ops extends IXingYiServerOps<IAddress> {
    String line1();

    String line2();

}

