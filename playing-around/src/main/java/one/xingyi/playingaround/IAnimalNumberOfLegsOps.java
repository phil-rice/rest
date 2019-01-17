package one.xingyi.playingaround;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiServerOps;
@XingYiOps
public interface IAnimalNumberOfLegsOps  extends IXingYiServerOps<IAnimal> {
    String numberOfLegs();

}
