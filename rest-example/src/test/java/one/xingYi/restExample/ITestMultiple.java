package one.xingYi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiCompositeInterface;
import one.xingyi.restAnnotations.clientside.IXingYiMultipleOps;
import one.xingyi.restExample.*;
//AHA Do IXingYiMultiple<T> and rip out the T.
@XingYiCompositeInterface()
interface ITestMultiple extends IXingYiMultipleOps<IPerson>, IPersonName, IPersonAddress {}
