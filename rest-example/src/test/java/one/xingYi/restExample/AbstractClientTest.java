package one.xingYi.restExample;
import one.xingyi.restAnnotations.access.IEntityStore;
import one.xingyi.restAnnotations.clientside.IXingYiResponseSplitter;
import one.xingyi.restAnnotations.clientside.JavaHttpClient;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.entity.EmbeddedWithHasJson;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.marshelling.JsonObject;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.server.EndpointHandler;
import one.xingyi.restAnnotations.server.SimpleServer;
import one.xingyi.restExample.*;
import one.xingyi.restcore.access.GetEntityEndpoint;
import one.xingyi.restcore.entity.EntityDetailsEndpoint;
import one.xingyi.restcore.entity.EntityRegister;
import one.xingyi.restcore.xingYiServer.*;
import one.xingyi.restcore.xingyiclient.XingYiClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

abstract class AbstractClientTest {
    String urlPrefix = "http://localhost:9000";

    TelephoneNumber number = new TelephoneNumber("someNumber");
    Address address = new Address("someLine1", "someLine2");
    Person person = new Person("name", address, EmbeddedWithHasJson.value(number));
    IEntityStore<Person> personStore = IEntityStore.map(Map.of("id1", person));
    IEntityStore<Address> addressStore = IEntityStore.map(Map.of("add1", address));

    JsonTC<JsonObject> jsonTC = JsonTC.cheapJson;

    EntityRegister register = EntityRegister.simple(EntityServerCompanion.companion, PersonServerCompanion.companion, AddressServerCompanion.companion, TelephoneNumberServerCompanion.companion);
    EndPoint entityDetailsEndPoint = EntityDetailsEndpoint.entityDetailsEndPoint(jsonTC, register);
    EndPoint getPersonEndpoint = GetEntityEndpoint.getOptionalEndPoint(jsonTC, register, PersonServerCompanion.companion, personStore::read);
    EndPoint getAddressEndpoint = GetEntityEndpoint.getOptionalEndPoint(jsonTC, register, AddressServerCompanion.companion, addressStore::read);

    EndPoint composed = EndPoint.compose(getAddressEndpoint, entityDetailsEndPoint, getPersonEndpoint);

    abstract protected Function<ServiceRequest, CompletableFuture<ServiceResponse>> httpClient();
    abstract protected String expectedHost();

    XingYiClient client = XingYiClient.using(urlPrefix, httpClient(),
            EntityClientCompanion.companion,
            PersonClientCompanion.companion,
            AddressClientCompanion.companion,
            TestMultipleClientCompanion.companion); //TODO Perhaps this can be automated?


    @Test
    public void testGetUsingUrl() throws ExecutionException, InterruptedException {
        assertEquals(expectedHost() + "/person/<id>", client.primitiveGet(IEntityUrlPattern.class, urlPrefix + "/person", e -> e.url()).get());
        assertEquals(expectedHost() + "/address/<id>", client.primitiveGet(IEntityUrlPattern.class, urlPrefix + "/address", e -> e.url()).get());
        assertEquals("[one.xingyi.restExample.IPersonAddressOps, one.xingyi.restExample.IPersonNameOps, one.xingyi.restExample.IPersonTelephoneNumberOps]", client.primitiveGet(IEntityInterfaces.class, urlPrefix + "/person", e -> e.interfaces()).get());
    }

    @Test
    public void testGetUrlPattern() throws ExecutionException, InterruptedException {
        assertEquals(expectedHost() + "/entity/<id>", client.getUrlPattern(IEntityUrlPattern.class).get());
        assertEquals(expectedHost() + "/person/<id>", client.getUrlPattern(IPersonNameOps.class).get());
        assertEquals(expectedHost() + "/address/<id>", client.getUrlPattern(IAddressLine12Ops.class).get());
    }

    @Test
    public void testGetPerson() throws ExecutionException, InterruptedException {
        assertEquals("name", client.get(IPersonNameOps.class, "id1", IPersonNameOps::name).get());
    }

    @Test
    public void testGetAddress() throws ExecutionException, InterruptedException {
        assertEquals(Optional.of(address), addressStore.read("add1").get());
        assertEquals("{'line1':'someLine1','line2':'someLine2'}".replace('\'', '"'), IXingYiResponseSplitter.splitter.apply(getAddressEndpoint.apply(new ServiceRequest("get", "/address/add1", Arrays.asList(), "")).get().get()).data);
        assertEquals("someLine1", client.get(IAddressLine12Ops.class, "add1", IAddressLine12Ops::line1).get());
    }

    static final String name = EntityClientImpl.class.getName();

    @Test
    public void testWithMultipleInterfaces() throws ExecutionException, InterruptedException {
        assertEquals("name/one.xi", client.primitiveGet(ITestMultiple.class, "http://localhost:9000/person/id1", e -> e.name() + "/" + e.address().toString().substring(0, 6)).get());
        //solution to this is to have a @XingYiMulti annotation and create instance which can delegate. Actually pretty straightforwards...
    }
    @Test
    public void testWithMultipleInterfaces2() throws ExecutionException, InterruptedException {
//    Thread.sleep(100000);
        assertEquals("name/one.xi", client.primitiveGet(ITestMultiple.class, "http://localhost:9000/person/id1", e -> e.name() + "/" + e.address().toString().substring(0, 6)).get());
        //solution to this is to have a @XingYiMulti annotation and create instance which can delegate. Actually pretty straightforwards...
    }
}
