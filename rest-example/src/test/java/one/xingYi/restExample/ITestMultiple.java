package one.xingYi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiCompositeInterface;
import one.xingyi.restExample.IPerson;
import one.xingyi.restExample.IPersonAddressOps;
import one.xingyi.restExample.IPersonNameOps;
import one.xingyi.restcore.xingYiServer.IEntityInterfaces;
import one.xingyi.restcore.xingYiServer.IEntityUrlPattern;
//TODO I would really like to get rid of this need to specify IPerson. Prehaps it can be infered from the interfaces?
@XingYiCompositeInterface("one.xingyi.restExample.IPerson")
interface ITestMultiple extends IPersonNameOps, IPersonAddressOps {}
