package one.xingyi.restcore.xingYiServer;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiServerOps;
@XingYiOps
public interface IEntityInterfacesOps extends IXingYiServerOps<IEntity> {
    String interfaces();
}
