package one.xingyi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiCompositeInterface;
import one.xingyi.restAnnotations.clientside.IXingYiMultipleOps;

//The full class definition is needed for the annotation processor because the server and client are in the same project
@XingYiCompositeInterface
public interface IPersonNameAndAddress extends IXingYiMultipleOps<IPerson>,one.xingyi.restExample.IPersonAddress, one.xingyi.restExample.IPersonName {
}
