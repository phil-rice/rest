package one.xingyi.restAnnotations.acceptHeader;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
public class SimpleAcceptHeaderParserTest {

    AcceptHeaderParser parser = AcceptHeaderParser.parser;

    @Test public void testIgnoresAnythingThatDoesntStartWithApplicationJson() {
        assertEquals(new AcceptHeaderDetails(false, List.of()), parser.apply(""));
        assertEquals(new AcceptHeaderDetails(false, List.of()), parser.apply("application"));
        assertEquals(new AcceptHeaderDetails(false, List.of()), parser.apply("application/xingyi"));
        assertEquals(new AcceptHeaderDetails(false, List.of()), parser.apply("application/xingyi.json1"));
        assertEquals(new AcceptHeaderDetails(false, List.of()), parser.apply("application/xingyi.json1.a.b"));
    }

    @Test public void testReturnsListOfNamesTrimmingPrefix() {
        assertEquals(new AcceptHeaderDetails(true, List.of("a", "b")), parser.apply("application/xingyi.json.lens_a.lens_b"));
        assertEquals(new AcceptHeaderDetails(true, List.of()), parser.apply("application/xingyi.json"));

    }
    @Test public void testBlowsUpWithoutLensPrefix() {
        try {
            parser.apply("application/xingyi.json.lens_a.b");
            fail();
        } catch (RuntimeException e) {
            assertEquals("java.lang.RuntimeException: Lens name 'b should start with 'lens_' the header was application/xingyi.json.lens_a.b", e.getMessage());
        }

    }

}