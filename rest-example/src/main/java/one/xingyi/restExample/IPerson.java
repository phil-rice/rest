package one.xingyi.restExample;

import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiField;

@XingYi()
// this makes all the methods available in person.
public interface IPerson { // this is no need for Person to extend IPerson. And this simplifies things a lot!
    @XingYiField(readInterfaces = Interfaces.personNameOps)
    String name(); //

    @XingYiField(interfaces = Interfaces.personTelephoneOps)
    ITelephoneNumber telephone();

    @XingYiField(interfaces = Interfaces.personAddressOps)
    IAddress address();

    @XingYiField(deprecated = true)
    String line1();


    @XingYiField(deprecated = true)
    String line2();
}
