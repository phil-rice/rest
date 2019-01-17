package one.xingyi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiField;
import one.xingyi.restAnnotations.annotations.XingYi;

@XingYi(urlPattern = "/address") // this makes all the methods available in person.
public interface IAddress extends IAddressLine12Ops {
    String line1();
    String line2();


}
