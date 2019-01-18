package one.xingYi.restExample;
import one.xingyi.restAnnotations.javascript.JavascriptStore;
import one.xingyi.restAnnotations.utils.Digestor;
import one.xingyi.restAnnotations.utils.Files;
import one.xingyi.restExample.PersonServer;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;
public class SimpleEntityRegisterTest {
        String rootTest = Files.getText("header.js");

    @Test
    public void testGetTheJavascriptInTheMaps() {
        assertEquals("function lens_IPerson_address_IAddress(){ return lens('address');};", PersonServer.register.javascriptFor("IPerson_address_IAddress"));
    }

    @Test
    public void testTheDeprecatedJsonAppears_AND_isTheJavascriptSpecified() {
        assertEquals("var line1Javascript=''", PersonServer.register.javascriptFor("IPerson_line1_String"));
    }

    @Test
    public void testHasDefaultJavascript() {
        assertEquals(rootTest, PersonServer.register.javascriptFor("root"));
    }

    @Test
    public void testCanGetJavascriptFromCreatedJavascriptStore() {
        JavascriptStore javascriptStore = JavascriptStore.finder(Digestor.sha256, PersonServer.register);
        assertEquals(rootTest+"\nfunction lens_IEntity_url_String(){ return lens('url');};\n" +
                "function lens_IEntity_interfaces_String(){ return lens('interfaces');};\n" +
                "function lens_IAddress_line1_String(){ return lens('line1');};\n" +
                "function lens_IAddress_line2_String(){ return lens('line2');};\n" +
                "function lens_IPerson_name_String(){ return lens('name');};\n" +
                "function lens_IPerson_address_IAddress(){ return lens('address');};\n" +
                "function lens_IPerson_telephone_EmbeddedITelephoneNumber(){ return lens('telephone');};\n" +
                "var line1Javascript=''\n" +
                "var line2Javascript=''\n" +
                "function lens_ITelephoneNumber_number_String(){ return lens('number');};", JavascriptStore.javascript(javascriptStore, Arrays.asList()));
    }
}