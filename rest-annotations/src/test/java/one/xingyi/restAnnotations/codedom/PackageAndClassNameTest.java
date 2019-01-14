package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.javascript.IXingYiFactory;
import org.junit.Test;

import static org.junit.Assert.*;
public class PackageAndClassNameTest {

    @Test
    public void testConstructor(){
        PackageAndClassName packageAndClassName = new PackageAndClassName(IXingYiFactory.class.getName());
        assertEquals(IXingYiFactory.class.getPackageName(), packageAndClassName.packageName);
        assertEquals(IXingYiFactory.class.getSimpleName() , packageAndClassName.className);
    }

}