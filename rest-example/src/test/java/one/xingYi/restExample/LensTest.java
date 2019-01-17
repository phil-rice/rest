package one.xingYi.restExample;
import one.xingyi.restExample.AddressLine12ClientCompanion;
import one.xingyi.restExample.PersonLine12ClientCompanion;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
public class LensTest {
    @Test public void testSimple() {
        assertEquals(Set.of("lens_IAddress_line1_String", "lens_IAddress_line2_String"), AddressLine12ClientCompanion.companion.lensNames());
    }
    @Test public void testDeprecated() {
        assertEquals(Set.of("lens_IPerson_line2_String","lens_IPerson_line1_String"), PersonLine12ClientCompanion.companion.lensNames());
    }
    @Test public void testMultiple(){
        assertEquals(Set.of("lens_IPerson_address_IAddress", "lens_IPerson_name_String"), TestMultipleClientCompanion.companion.lensNames());

    }
}
