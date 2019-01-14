package one.xingYi.restExample;
import one.xingyi.restAnnotations.annotations.XingYiCompositeInterface;
import one.xingyi.restExample.IPersonAddressOps;
import one.xingyi.restExample.IPersonNameOps;
import one.xingyi.restcore.xingYiServer.IEntityInterfaces;
import one.xingyi.restcore.xingYiServer.IEntityUrlPattern;
@XingYiCompositeInterface("one.xingyi.restExample.PersonClientImpl")
interface ITestMultiple extends IPersonNameOps, IPersonAddressOps {}
