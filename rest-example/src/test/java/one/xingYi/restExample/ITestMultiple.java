package one.xingYi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiCompositeInterface;
import one.xingyi.restAnnotations.clientside.IXingYiMultipleOps;
import one.xingyi.restExample.*;
//TODO I would really like to get rid of this need to specify IPerson. Prehaps it can be infered from the interfaces?
//AHA Do IXingYiMultiple<T> and rip out the T.
@XingYiCompositeInterface()
interface ITestMultiple extends IXingYiMultipleOps<IPerson>, IPersonName, IPersonAddress {}
