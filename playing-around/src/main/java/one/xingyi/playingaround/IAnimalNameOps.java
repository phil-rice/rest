package one.xingyi.playingaround;
import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiServerOps;
@XingYiOps
public interface IAnimalNameOps extends IXingYiServerOps<IAnimal> {
    String name();
}
