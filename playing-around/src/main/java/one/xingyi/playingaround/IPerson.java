package one.xingyi.playingaround;
import one.xingyi.restAnnotations.annotations.XingYi;
@XingYi
public interface IPerson extends IPersonNameOps, IPersonAddressOps {
    String name();
    String line1();
    String line2();
}
