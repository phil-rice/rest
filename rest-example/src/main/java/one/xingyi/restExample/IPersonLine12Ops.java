package one.xingyi.restExample;
import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiField;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiServerOps;

@XingYiOps
public interface IPersonLine12Ops extends IXingYiServerOps<IPerson> {
    @Deprecated @XingYiField(deprecated = true) String line1();
    @Deprecated @XingYiField(deprecated = true) String line2();
}
