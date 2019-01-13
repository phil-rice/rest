package one.xingyi.restAnnotations.utils;
import one.xingyi.restAnnotations.entity.Embedded;
import org.junit.Test;

import static org.junit.Assert.*;
public class StringsTest {
    @Test
    public void testExtractWhenPresent() {
        String actual = Strings.extractFromOptionalEnvelope(Embedded.class.getName(), ">", Embedded.class.getName()+"<one.xingyi.restExample.ITelephoneNumber>");
        assertEquals("one.xingyi.restExample.ITelephoneNumber", actual);
    }
    @Test
    public void testExtractWhenNotPresentAtEnd() {
        String actual = Strings.extractFromOptionalEnvelope(Embedded.class.getName(), ">", "one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber");
        assertEquals("one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber", actual);
    }
    @Test
    public void testExtractWhenNotPresentAtStart() {
        String actual = Strings.extractFromOptionalEnvelope(Embedded.class.getName(), ">", "one.xingyi.rty.Embedded<one.xingyi.restExample.ITelephoneNumber>");
        assertEquals("one.xingyi.rty.Embedded<one.xingyi.restExample.ITelephoneNumber>", actual);
    }
}