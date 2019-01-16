package one.xingyi.sampleServer;
import one.xingyi.restAnnotations.access.IEntityStore;
import one.xingyi.restAnnotations.entity.EmbeddedWithHasJson;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.marshelling.JsonObject;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.server.EndpointHandler;
import one.xingyi.restAnnotations.server.HttpUtils;
import one.xingyi.restAnnotations.server.SimpleServer;
import one.xingyi.restExample.*;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.endpoints.EndpointAcceptor0;
import one.xingyi.restcore.access.GetEntityEndpoint;
import one.xingyi.restcore.entity.EntityDetailsEndpoint;
import one.xingyi.restcore.entity.EntityRegister;
import one.xingyi.restcore.xingYiServer.Entity;
import one.xingyi.restcore.xingYiServer.EntityServerCompanion;

import java.util.Map;

public class SampleServer {

    //These are the 'fake' backend values. Normally this would be a link to a database or another microservice
    TelephoneNumber number = new TelephoneNumber("someNumber");
    Address address = new Address("someLine1", "someLine2");
    Person person = new Person("serverName", address, EmbeddedWithHasJson.value(number));
    IEntityStore<Person> personStore = IEntityStore.map(Map.of("id1", person));
    IEntityStore<Address> addressStore = IEntityStore.map(Map.of("add1", address));

    //How we do Json. This uses a really quick and dirty Json printer
    JsonTC<JsonObject> jsonTC = JsonTC.cheapJson;

    //objects that the backend will be serving up. This is needed for the entity details end points so it knows how to tell the client where to find them
    EntityRegister register = EntityRegister.simple(
            EntityServerCompanion.companion,
            PersonServerCompanion.companion,
            AddressServerCompanion.companion,
            TelephoneNumberServerCompanion.companion);

    //This serves the bookmarked urls used by the entities
    EndPoint entityDetailsEndPoint = EntityDetailsEndpoint.entityDetailsEndPoint(jsonTC, register);

    EndPoint getPersonEndpoint = GetEntityEndpoint.getOptionalEndPoint(jsonTC, register, PersonServerCompanion.companion, personStore::read);
    EndPoint getAddressEndpoint = GetEntityEndpoint.getOptionalEndPoint(jsonTC, register, AddressServerCompanion.companion, addressStore::read);

    //These are just for debugging
    EndPoint index = EndPoint.function(EndpointAcceptor0.exact("get", "/"), sr -> ServiceResponse.html(200, "made it: you sent" + sr));
    EndPoint keepalive = EndPoint.staticEndpoint(EndpointAcceptor0.exact("get", "/keepalive"), ServiceResponse.html(200, "Alive"));

    //Now the actual server
    EndPoint composed = EndPoint.compose(index, keepalive, entityDetailsEndPoint, getAddressEndpoint, getPersonEndpoint, getAddressEndpoint);
    EndPoint all = EndPoint.printlnLog(composed);

    //Every up until this point has been independent of the web framework used. Endpoint handler is the 'wrapper' that lifts the endpoint into this framework
    SimpleServer server = new SimpleServer(HttpUtils.makeDefaultExecutor(), new EndpointHandler(composed), 9000);

    public static void main(String[] args) {
        new SampleServer().server.start();
    }
}
