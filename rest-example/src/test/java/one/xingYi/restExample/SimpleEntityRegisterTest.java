package one.xingYi.restExample;
import one.xingyi.restAnnotations.utils.Files;
import one.xingyi.restExample.PersonServer;
import org.junit.Test;

import static org.junit.Assert.*;
public class SimpleEntityRegisterTest {

    @Test
    public void testGetTheJavascriptInTheMaps() {
        assertEquals("function lens_IPerson_address_IAddress(){ return lens('address');};", PersonServer.register.javascriptFor("IPerson_address_IAddress"));
    }

    @Test
    public void testTheDeprecatedJsonAppears_AND_isTheJavascriptSpecified() {
        assertEquals("line1Javascript", PersonServer.register.javascriptFor("IPerson_line1_String"));
    }

    @Test
    public void testHasDefaultJavascript() {
        assertEquals(Files.getText("header.js"), PersonServer.register.javascriptFor("root"));
    }
}