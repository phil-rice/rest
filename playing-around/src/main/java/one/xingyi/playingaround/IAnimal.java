package one.xingyi.playingaround;
import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiField;

@XingYi(urlPattern = "/animal")
public interface IAnimal extends IAnimalNameOps, IAnimalNumberOfLegsOps{
    String name();
    String numberOfLegs();
}
