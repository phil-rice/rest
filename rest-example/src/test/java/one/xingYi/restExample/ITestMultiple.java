package one.xingYi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiCompositeInterface;
import one.xingyi.restExample.IPersonAddress;
import one.xingyi.restExample.IPersonAddressOps;
import one.xingyi.restExample.IPersonName;
import one.xingyi.restExample.IPersonNameOps;
//TODO I would really like to get rid of this need to specify IPerson. Prehaps it can be infered from the interfaces?
@XingYiCompositeInterface("one.xingyi.restExample.IPerson")
interface ITestMultiple extends IPersonName, IPersonAddress {}
