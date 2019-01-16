package one.xingyi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiServerOps;
import one.xingyi.restAnnotations.entity.Embedded;
@XingYiOps
public interface IPersonTelephoneNumberOps extends IXingYiServerOps<IPerson> {
    Embedded<ITelephoneNumber> telephone();
}
