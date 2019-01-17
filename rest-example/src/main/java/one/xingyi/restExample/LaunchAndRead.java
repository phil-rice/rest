package one.xingyi.restExample;
import one.xingyi.restAnnotations.access.IEntityStore;
import one.xingyi.restAnnotations.client.Client;
import one.xingyi.restAnnotations.clientside.JavaHttpClient;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.entity.EmbeddedWithHasJson;
import one.xingyi.restAnnotations.marshelling.JsonObject;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.server.SimpleServer;

import java.util.Map;
public class LaunchAndRead {

    static TelephoneNumber number = new TelephoneNumber("someNumber");
    static Address address = new Address("someLine1", "someLine2");
    static Person person = new Person("the persons name", address, EmbeddedWithHasJson.valueForTest(number));

    static IEntityStore<Person> personStore = IEntityStore.map(Map.of("id1", person));
    static IEntityStore<Address> addressStore = IEntityStore.map(Map.of("add1", address));

    static int port = 9000;
    public static JsonTC<JsonObject> jsonTC = JsonTC.cheapJson;
    public static EndPoint endPoints = PersonServer.createEndpoints(jsonTC, addressStore, personStore);//the stores are  just ways of 'given an id go get a completable future of xxx' a


    public static void main(String[] args) {
        Client client = DomainClient.client("http://localhost:" + port, JavaHttpClient.client, PersonNameAndAddressClientCompanion.companion);
        SimpleServer.doAndThenStop(port, endPoints, server -> {
            System.out.println(client.get(IPersonNameAndAddress.class, "id1", e -> e.name() + "/" + e.address().line1()).get());
        });
    }
}
