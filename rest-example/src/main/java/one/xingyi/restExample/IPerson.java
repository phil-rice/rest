package one.xingyi.restExample;

import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiField;
import one.xingyi.restAnnotations.entity.Embedded;

import java.util.HashMap;
import java.util.Map;

@XingYi(urlPattern = "/person")
public interface IPerson extends IPersonNameOps, IPersonLine12Ops, IPersonAddressOps {
    String name();
    IAddress address();
    Embedded<ITelephoneNumber> telephone();
    @Deprecated @XingYiField(deprecated = true, javascript = "var line1Javascript=''") default String line1() {throw new RuntimeException("not implemented");}
    @Deprecated @XingYiField(deprecated = true, javascript = "var line2Javascript=''") default String line2() {throw new RuntimeException("not implemented");}

}
