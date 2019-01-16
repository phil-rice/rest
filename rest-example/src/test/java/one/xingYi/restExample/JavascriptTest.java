package one.xingYi.restExample;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.entity.EmbeddedWithHasJson;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.javascript.IXingYiFactory;
import one.xingyi.restAnnotations.marshelling.ContextForJson;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.utils.Files;
import one.xingyi.restExample.*;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
public class JavascriptTest {

    TelephoneNumber number = new TelephoneNumber("someNumber");
    Address address = new Address("someLine1", "someLine2");
    Person person = new Person("serverName", address, EmbeddedWithHasJson.valueForTest(number));
    Person personOtherName = new Person("otherName", address, EmbeddedWithHasJson.valueForTest(number));

    ServiceRequest serviceRequest = new ServiceRequest("get", "http://somehost", Arrays.asList(), "");
    ContextForJson context = new ContextForJson(serviceRequest);
    @Test
    public void testGetters() {
        String javascript = Files.getText("header.js") +
                PersonServerCompanion.companion.javascript() +
                AddressServerCompanion.companion.javascript() +
                TelephoneNumberServerCompanion.companion.javascript();
        IXingYi xingYi = IXingYiFactory.xingYi.apply(javascript);
        String json = person.toJsonString(JsonTC.cheapJson, context);
        Object mirror = xingYi.parse(json);
        PersonClientImpl client = new PersonClientImpl(mirror, xingYi);
        assertEquals("serverName", client.name());
        assertEquals("someLine1", client.address().line1());
    }

    @Test
    public void testGetters2() {
        String javascript = Files.getText("header.js") +
                PersonServerCompanion.companion.javascript() +
                AddressServerCompanion.companion.javascript() +
                TelephoneNumberServerCompanion.companion.javascript();
        IXingYi xingYi = IXingYiFactory.xingYi.apply(javascript);
        String json = person.toJsonString(JsonTC.cheapJson,context);
        Object mirror = xingYi.parse(json);
        PersonClientImpl client = new PersonClientImpl(mirror, xingYi);
        assertEquals("serverName", client.name());
        assertEquals("someLine1", client.address().line1());
    }


}
