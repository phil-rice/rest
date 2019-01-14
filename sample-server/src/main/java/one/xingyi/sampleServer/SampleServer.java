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

    public static void main(String[] args) {
        String urlPrefix = "http://localhost:9000";

        TelephoneNumber number = new TelephoneNumber("someNumber");
        Address address = new Address("someLine1", "someLine2");
        Person person = new Person("name", address, EmbeddedWithHasJson.value(number));
        IEntityStore<Person> personStore = IEntityStore.map(Map.of("id1", person));
        IEntityStore<Address> addressStore = IEntityStore.map(Map.of("add1", address));

        JsonTC<JsonObject> jsonTC = JsonTC.cheapJson;

        EntityRegister register = EntityRegister.simple(
                EntityServerCompanion.companion,
                PersonServerCompanion.companion,
                AddressServerCompanion.companion,
                TelephoneNumberServerCompanion.companion);
        EndPoint entityDetailsEndPoint = EntityDetailsEndpoint.entityDetailsEndPoint(jsonTC, register);


        EndPoint getPersonEndpoint = GetEntityEndpoint.getOptionalEndPoint(jsonTC, PersonServerCompanion.companion, personStore::read);
        EndPoint getAddressEndpoint = GetEntityEndpoint.getOptionalEndPoint(jsonTC, AddressServerCompanion.companion, addressStore::read);

        EndPoint index = EndPoint.function(EndpointAcceptor0.exact("get", "/"), sr -> ServiceResponse.html(200, "made it: you sent" + sr));
        EndPoint keepalive = EndPoint.staticEndpoint(EndpointAcceptor0.exact("get", "/keepalive"), ServiceResponse.html(200, "Alive"));

        EndPoint composed = EndPoint.compose(entityDetailsEndPoint, getAddressEndpoint, getPersonEndpoint, getAddressEndpoint);//, index, keepalive);


        EndPoint all = EndPoint.printlnLog(composed);
        SimpleServer server = new SimpleServer(HttpUtils.makeDefaultExecutor(), new EndpointHandler(composed), 9000);
        server.start();
    }
}
