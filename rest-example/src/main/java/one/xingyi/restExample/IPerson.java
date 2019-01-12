package one.xingyi.restExample;

import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiField;

import java.util.HashMap;
import java.util.Map;

@XingYi()
public interface IPerson {
    @XingYiField(readInterfaces = Interfaces.personNameOps)
    String name(); //

    @XingYiField(interfaces = Interfaces.personAddressOps)
    IAddress address();

    @XingYiField(interfaces = Interfaces.personTelephoneOps)
    ITelephoneNumber telephone();


    @XingYiField(deprecated = true)
    default String line1() {throw new RuntimeException("not implemented");}
    ;

    @XingYiField(deprecated = true)
    default String line2() {throw new RuntimeException("not implemented");}
    ;

}
