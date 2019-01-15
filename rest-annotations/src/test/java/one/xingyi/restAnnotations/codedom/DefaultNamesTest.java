package one.xingyi.restAnnotations.codedom;

import one.xingyi.restAnnotations.names.INames;
import org.junit.Test;

import javax.lang.model.element.Element;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
public class DefaultNamesTest {


    final INames names = INames.defaultNames;
    final String sampleType = "one.xingyi.restcore.xingYiServer.IEntity";
    final String sampleSimpleName= "IEntity";
    @Test
    public void testCanGetSimpleMethods() {
        Element element = mock(Element.class, RETURNS_DEEP_STUBS);
        when(element.asType().toString()).thenReturn(sampleType);
        when(element.getSimpleName().toString()).thenReturn(sampleSimpleName);

       assertEquals(new PackageAndClassName("one.xingyi.restcore.xingYiServer", "IEntity"),names.getEntity(element));
    }

}
