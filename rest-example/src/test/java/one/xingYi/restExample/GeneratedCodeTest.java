package one.xingYi.restExample;
import one.xingyi.restExample.Address;
import one.xingyi.restExample.Person;
import one.xingyi.restExample.TelephoneNumber;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class GeneratedCodeTest {

    TelephoneNumber number = new TelephoneNumber("someNumber");
    Address address = new Address("someLine1", "someLine2");
    Person person = new Person("name", number, address);
    Person personOtherName = new Person("otherName", number, address);

    @Test
    public void testCanDoThingsWithPerson() {
        assertEquals("name", person.name());
        assertEquals(personOtherName, person.withName("otherName"));
    }
}
