package one.xingyi.restAnnotations.clientside;
import one.xingyi.restAnnotations.javascript.IXingYi;

import java.util.Optional;
public interface IClientMaker {
    <Interface> Optional<Interface> apply(Class<Interface> clazz, IXingYi xingYi, Object mirror);

}
