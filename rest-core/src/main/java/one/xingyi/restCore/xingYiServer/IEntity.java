package one.xingyi.restcore.xingYiServer;
import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiField;


@XingYi() // this makes all the methods available in person.
public interface IEntity extends IEntityUrlPattern, IEntityInterfaces {
    @XingYiField(templatedJson = true)
    String url();

    String interfaces();

}
