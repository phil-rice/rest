package one.xingyi.playingaround;
import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiField;
@XingYi
public interface IPerson extends IPersonNameOps, IPersonLine1L2Ops {
    @XingYiField(readOnly = true)
    String name();
    IAddress address();
    String line1();
    String line2();
}
