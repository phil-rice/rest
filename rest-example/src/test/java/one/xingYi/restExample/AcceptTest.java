package one.xingYi.restExample;
import one.xingyi.restExample.AddressLine12ClientCompanion;
import one.xingyi.restExample.PersonLine12ClientCompanion;
import org.junit.Test;

import java.util.Set;

import static junit.framework.TestCase.assertEquals;
public class AcceptTest {
    @Test public void testSimple() {
        assertEquals("application/xingyi.lens_IAddress_line1_String.lens_IAddress_line2_String", AddressLine12ClientCompanion.companion.acceptString());
    }
    @Test public void testDeprecated() {
        assertEquals("application/xingyi.lens_IPerson_line1_String.lens_IPerson_line2_String", PersonLine12ClientCompanion.companion.acceptString());
    }
    @Test public void testMultiple(){
        assertEquals("application/xingyi.lens_IPerson_address_IAddress.lens_IPerson_name_String", TestMultipleClientCompanion.companion.acceptString());

    }
}
