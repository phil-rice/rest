package one.xingYi.restExample;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.utils.Files;
import one.xingyi.restExample.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class JavascriptTest {

    TelephoneNumber number = new TelephoneNumber("someNumber");
    Address address = new Address("someLine1", "someLine2");
    Person1 person = new Person1("name", address, number);
    Person1 personOtherName = new Person1("otherName", address, number);

    @Test
    public void testGetters() {
        String javascript = Files.getText("header.js") +
                PersonServerCompanion.companion.javascript() +
                AddressServerCompanion.companion.javascript() +
                TelephoneNumberServerCompanion.companion.javascript();
        IXingYi xingYi = IXingYi.create(javascript);
        String json = person.toJsonString(JsonTC.cheapJson);
        PersonClientImpl client = xingYi.parse(PersonClientImpl.class, json);
        assertEquals("name", client.name());
        assertEquals("someLine1", client.address().line1());
    }


}
