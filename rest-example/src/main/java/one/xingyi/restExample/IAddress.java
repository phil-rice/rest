package one.xingyi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiField;
import one.xingyi.restAnnotations.annotations.XingYi;

@XingYi() // this makes all the methods available in person.
public interface IAddress extends IAddressLine12Ops {
    //    public static final IAddress protoype = new Address("", "");
//    @XingYiField(interfaces = Interfaces.addressLine12Ops)
    String line1();

//    @XingYiField(interfaces = Interfaces.addressLine12Ops)
    String line2();


}
