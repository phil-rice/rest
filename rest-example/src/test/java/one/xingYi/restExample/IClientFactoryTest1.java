package one.xingYi.restExample;
import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiCompositeInterface;
import one.xingyi.restAnnotations.annotations.XingYiField;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiOps;
@XingYi
public interface IClientFactoryTest1 {
    String name1();
    String name2();
}
@XingYiOps
interface ITest11 extends IXingYiOps<IClientFactoryTest1> {
    String name1();

}
@XingYiOps
interface ITest12 extends IXingYiOps<IClientFactoryTest1> {
    String name2();

}
@XingYiOps
interface ITest22 extends IXingYiOps<IClientFactoryTest2> {
    String name2();

}
@XingYi
interface IClientFactoryTest2 {
    String name2();

}

//Cannot do this in this execution phase I think
//
// @XingYiCompositeInterface(ClientFactoryTest1ClientImpl.class)
//interface IClientFactoryTest12 extends ITest11, ITest12 {
//
//}