package one.xingyi.restcore.xingYiServer;
import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiField;


@XingYi() // this makes all the methods available in person.
public interface IEntity {
    @XingYiField(readInterfaces = EntityInterfaces.entityUrlPattern, templatedJson = true)
    String url();

    @XingYiField(readInterfaces = EntityInterfaces.entityInterfaces)
    String interfaces();

}
