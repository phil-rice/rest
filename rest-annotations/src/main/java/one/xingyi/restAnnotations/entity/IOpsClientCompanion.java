package one.xingyi.restAnnotations.entity;
import one.xingyi.restAnnotations.clientside.IClientCompanion;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.javascript.XingYiDomain;

import java.util.Set;
public interface  IOpsClientCompanion<T extends XingYiDomain> {
    IClientCompanion entityCompanion();
    Set<String> lensNames();
    T makeImplementation(IXingYi xingYi, Object mirror);

}
