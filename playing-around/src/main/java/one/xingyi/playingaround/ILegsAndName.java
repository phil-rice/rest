package one.xingyi.playingaround;

import one.xingyi.restAnnotations.annotations.XingYiCompositeInterface;
import one.xingyi.restAnnotations.clientside.IXingYiMultipleOps;
@XingYiCompositeInterface
public interface ILegsAndName extends IXingYiMultipleOps<IAnimal>,one.xingyi.playingaround.IAnimalNumberOfLegs, one.xingyi.playingaround.IAnimalName {
}
