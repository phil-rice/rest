package one.xingyi.restcore.xingYiServer;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiOps;
@XingYiOps
public interface IEntityUrlPatternOps extends IXingYiOps<IEntity> {
    String url();
}
