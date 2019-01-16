package one.xingYi.restExample;
import one.xingyi.restAnnotations.entity.EmbeddedWithHasJson;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.marshelling.ContextForJson;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restExample.Address;
import one.xingyi.restExample.Person;
import one.xingyi.restExample.TelephoneNumber;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
public class GeneratedCodeTest {

    TelephoneNumber number = new TelephoneNumber("someNumber");
    Address address = new Address("someLine1", "someLine2");
    Person person = new Person("serverName", address, EmbeddedWithHasJson.<TelephoneNumber>value(number));
    Person personOtherName = new Person("otherName", address, EmbeddedWithHasJson.value(number));
    ServiceRequest serviceRequest = new ServiceRequest("get", "http://somehost", Arrays.asList(), "");
    ContextForJson context = new ContextForJson(serviceRequest);

    @Test
    public void testCanDoThingsWithPerson() {
        assertEquals("serverName", person.name());
        assertEquals(personOtherName, person.withName("otherName"));
    }

    @Test
    public void testGeneratedJson_map() {
        assertEquals("{name=serverName, address={line1=someLine1, line2=someLine2}, telephone={_embedded={number=someNumber}}}",
                JsonTC.forMaps.toJson(person, context));
    }
    @Test
    public void testGeneratedJson_string() {
        assertEquals("{'name':'serverName','address':{'line1':'someLine1','line2':'someLine2'},'telephone':{'_embedded':{'number':'someNumber'}}}",
                JsonTC.cheapJson.toJson(person, context).replace('"', '\''));
    }
}
