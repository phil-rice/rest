package one.xingYi.restExample;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restExample.Address;
import one.xingyi.restExample.TelephoneNumber;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class GeneratedCodeTest {

    TelephoneNumber number = new TelephoneNumber("someNumber");
    Address address = new Address("someLine1", "someLine2");
    Person1 person = new Person1("name", address, number);
    Person1 personOtherName = new Person1("otherName", address, number);

    @Test
    public void testCanDoThingsWithPerson() {
        assertEquals("name", person.name());
        assertEquals(personOtherName, person.withName("otherName"));
    }

    @Test
    public void testGeneratedJson_map() {
        assertEquals("{name=name, address={line1=someLine1, line2=someLine2}, telephone={number=someNumber}}",
                JsonTC.forMaps.toJson(person));
    }
    @Test
    public void testGeneratedJson_string() {
        assertEquals("{'name':'name','address':{'line1':'someLine1','line2':'someLine2'},'telephone':{'number':'someNumber'}}",
                JsonTC.cheapJson.toJson(person).replace('"', '\''));
    }
}
