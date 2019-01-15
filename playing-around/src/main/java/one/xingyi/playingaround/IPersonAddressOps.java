package one.xingyi.playingaround;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiOps;
@XingYiOps
public interface IPersonAddressOps extends IXingYiOps<IPerson> {
    String line1();
    String line2();
}
