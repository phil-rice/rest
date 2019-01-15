package one.xingyi.restcore.xingYiServer;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiOps;
@XingYiOps
public interface IEntityUrlPattern extends IXingYiOps<IEntity> {
    String url();
}
