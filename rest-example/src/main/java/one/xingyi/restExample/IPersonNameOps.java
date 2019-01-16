package one.xingyi.restExample;

import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiServerOps;
@XingYiOps
public interface IPersonNameOps extends IXingYiServerOps<IPerson> {
    String name();
}
