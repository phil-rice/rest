package one.xingyi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiOps;
import one.xingyi.restAnnotations.entity.Embedded;
@XingYiOps
public interface IPersonTelephoneNumberOps extends IXingYiOps<IPerson> {
    Embedded<ITelephoneNumber> telephone();
}
