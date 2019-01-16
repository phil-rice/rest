package one.xingyi.playingaround;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiOps;
@Deprecated
@XingYiOps
public interface IPersonAddressOps extends IXingYiOps<IPerson> {
    @Deprecated
    String line1();
    @Deprecated
    String line2();
}
