package one.xingyi.restExample;
import one.xingyi.restAnnotations.access.IEntityStore;
import one.xingyi.restAnnotations.entity.EmbeddedWithHasJson;

import java.util.Map;
//These are the 'fake' backend values. Normally this would be a link to a database or another microservice
public interface MockPersonAndAddressStores {
    TelephoneNumber number = new TelephoneNumber("someNumber");
    Address address = new Address("someLine1", "someLine2");
    Person person = new Person("the persons name", address, EmbeddedWithHasJson.valueForTest(number));

    IEntityStore<Person> personStore = IEntityStore.map(Map.of("id1", person));
    IEntityStore<Address> addressStore = IEntityStore.map(Map.of("add1", address));
}