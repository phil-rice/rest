package one.xingyi.restExample;
import one.xingyi.restAnnotations.XingYi;

@XingYi() // this makes all the methods available in person.
public interface IAddress {
    //    public static final IAddress protoype = new Address("", "");
    @XingYi(Interfaces.addressLine12Ops)
    String line1();

    @XingYi(Interfaces.addressLine12Ops)
    String line2();


}
