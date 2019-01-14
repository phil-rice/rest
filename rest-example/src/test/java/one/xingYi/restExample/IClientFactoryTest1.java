package one.xingYi.restExample;
import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiField;
@XingYi
public interface IClientFactoryTest1 {
    @XingYiField(interfaces = {"ITest11", "ITest12"})
    String name1();
}
@XingYi
interface IClientFactoryTest2 {
    @XingYiField(interfaces = "ITest22")
    String name2();

}
