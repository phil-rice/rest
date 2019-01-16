package one.xingyi.playingaround;
import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiField;
@XingYi
public interface IPerson extends IPersonNameOps, IPersonAddressOps {
    @XingYiField(readOnly = true)
    String name();
    String line1();
    String line2();
}
