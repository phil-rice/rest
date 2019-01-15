package one.xingyi.restExample;

import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiOps;
@XingYiOps
public interface IPersonNameOps extends IXingYiOps<IPerson> {
    String name();
}
