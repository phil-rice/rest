package one.xingyi.playingaround;
import one.xingyi.restAnnotations.annotations.XingYi;
import one.xingyi.restAnnotations.annotations.XingYiOps;
import one.xingyi.restAnnotations.clientside.IXingYiOps;
import one.xingyi.restAnnotations.javascript.IXingYiFactory;

import java.util.Stack;
@XingYi
public interface IAddress {
    String line1();
    String line2();
}
