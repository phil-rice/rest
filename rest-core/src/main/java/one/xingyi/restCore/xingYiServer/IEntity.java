package one.xingyi.restCore.xingYiServer;
import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiField;


@XingYi() // this makes all the methods available in person.
public interface IEntity {
    @XingYiField(readInterfaces = EntityInterfaces.entityUrlPattern)
    String url();

}
