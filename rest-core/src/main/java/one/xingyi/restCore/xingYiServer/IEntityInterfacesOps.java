package one.xingyi.restcore.xingYiServer;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiOps;
@XingYiOps
public interface IEntityInterfacesOps extends IXingYiOps<IEntity> {
    String interfaces();
}
