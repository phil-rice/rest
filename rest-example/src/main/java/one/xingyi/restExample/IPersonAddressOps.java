package one.xingyi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiServerOps;

@XingYiOps
public interface IPersonAddressOps extends IXingYiServerOps<IPerson> {
    IAddress address();
}
