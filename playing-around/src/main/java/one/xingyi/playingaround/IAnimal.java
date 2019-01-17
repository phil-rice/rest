package one.xingyi.playingaround;
import one.xingyi.restAnnotations.annotations.XingYi;
@XingYi(urlPattern = "/animal")
public interface IAnimal extends IAnimalNameOps{
    String name();
}
