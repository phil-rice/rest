package one.xingYi.restExample;
import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiServerOps;
@XingYi
public interface IClientFactoryTest1 {
    String name1();
    String name2();
}
@XingYiOps
interface ITest11Ops extends IXingYiServerOps<IClientFactoryTest1> {
    String name1();

}
@XingYiOps
interface ITest12Ops extends IXingYiServerOps<IClientFactoryTest1> {
    String name2();

}
@XingYiOps
interface ITest22Ops extends IXingYiServerOps<IClientFactoryTest2> {
    String name2();

}
@XingYi
interface IClientFactoryTest2 {
    String name2();

}

//Cannot do this in this execution phase I think
//
// @XingYiCompositeInterface(ClientFactoryTest1ClientImplOps.class)
//interface IClientFactoryTest12 extends ITest11Ops, ITest12Ops {
//
//}