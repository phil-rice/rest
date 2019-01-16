package one.xingyi.restExample;

import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiField;
import one.xingyi.restAnnotations.entity.Embedded;

import java.util.HashMap;
import java.util.Map;

@XingYi(urlPattern = "/person")
public interface IPerson extends IPersonNameOps, IPersonLine12Ops, IPersonAddressOps {
    //    @XingYiField(readOnly = true)
    String name(); //

    //    @XingYiField(interfaces = Interfaces.personAddressOps)
    IAddress address();

    //    @XingYiField(interfaces = Interfaces.personTelephoneOps)
    Embedded<ITelephoneNumber> telephone();


    @XingYiField(deprecated = true)
    default String line1() {throw new RuntimeException("not implemented");}

    @XingYiField(deprecated = true)
    default String line2() {throw new RuntimeException("not implemented");}

}
