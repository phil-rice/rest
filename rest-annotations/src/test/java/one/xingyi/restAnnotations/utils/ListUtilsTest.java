package one.xingyi.restAnnotations.utils;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
public class ListUtilsTest {
    @Test
    public void testUnique() {
        assertEquals(Arrays.asList("1", "2", "3", "4"), ListUtils.unique(Arrays.asList("1", "2", "3", "4", "1", "2", "3", "4")));
    }
}
