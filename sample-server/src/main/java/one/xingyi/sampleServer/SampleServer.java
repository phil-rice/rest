package one.xingyi.sampleServer;
import one.xingyi.restAnnotations.access.IEntityStore;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.entity.EmbeddedWithHasJson;
import one.xingyi.restAnnotations.marshelling.JsonObject;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.server.EndpointHandler;
import one.xingyi.restAnnotations.server.HttpUtils;
import one.xingyi.restAnnotations.server.SimpleServer;
import one.xingyi.restExample.Address;
import one.xingyi.restExample.Person;
import one.xingyi.restExample.PersonServer;
import one.xingyi.restExample.TelephoneNumber;

import java.util.Map;

//These are the 'fake' backend values. Normally this would be a link to a database or another microservice
interface MockPersonAndAddressStores {
    TelephoneNumber number = new TelephoneNumber("someNumber");
    Address address = new Address("someLine1", "someLine2");
    Person person = new Person("serverName", address, EmbeddedWithHasJson.valueForTest(number));

    IEntityStore<Person> personStore = IEntityStore.map(Map.of("id1", person));
    IEntityStore<Address> addressStore = IEntityStore.map(Map.of("add1", address));
}

public class SampleServer implements MockPersonAndAddressStores {

    public static void main(String[] args) {
        JsonTC<JsonObject> jsonTC = JsonTC.cheapJson;
        EndPoint endPoints = PersonServer.createWithHelpers(jsonTC, addressStore, personStore);//the stores are  just ways of 'given an id go get a completable future of xxx' a
        SimpleServer server = new SimpleServer(HttpUtils.makeDefaultExecutor(), new EndpointHandler(endPoints), 9000);
        server.start();
    }
}
