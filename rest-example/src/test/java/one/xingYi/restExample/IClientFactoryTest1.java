package one.xingYi.restExample;
import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiCompositeInterface;
import one.xingyi.restAnnotations.annotations.XingYiField;
@XingYi
public interface IClientFactoryTest1 {
    @XingYiField(interfaces = {"ITest11"})
    String name1();

    @XingYiField(interfaces = {"ITest12"})
    String name2();
}
@XingYi
interface IClientFactoryTest2 {
    @XingYiField(interfaces = "ITest22")
    String name2();

}

//Cannot do this in this execution phase I think
//
// @XingYiCompositeInterface(ClientFactoryTest1ClientImpl.class)
//interface IClientFactoryTest12 extends ITest11, ITest12 {
//
//}