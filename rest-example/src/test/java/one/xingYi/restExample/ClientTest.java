package one.xingYi.restExample;
import one.xingyi.restAnnotations.access.IEntityStore;
import one.xingyi.restAnnotations.clientside.IClientFactory;
import one.xingyi.restAnnotations.clientside.IClientMaker;
import one.xingyi.restAnnotations.clientside.IXingYiResponseSplitter;
import one.xingyi.restAnnotations.entity.EmbeddedWithHasJson;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restAnnotations.marshelling.JsonObject;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restExample.*;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restcore.access.GetEntityEndpoint;
import one.xingyi.restcore.entity.EntityDetailsEndpoint;
import one.xingyi.restcore.entity.EntityRegister;
import one.xingyi.restcore.xingYiServer.*;
import one.xingyi.restcore.xingyiclient.XingYiClient;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
public class ClientTest {
    IClientFactory testMultipleBodge = new IClientFactory() {
        @Override public Set<Class<?>> supported() {
            return Set.of(ITestMultiple.class);
        }
        @Override public Function<Class<?>, Optional<IClientMaker>> findCompanion() {
            return clazz -> {
                if (supported().contains(clazz))
                    return Optional.of(new IClientMaker() {
                        @Override public <Interface> Optional<Interface> apply(Class<Interface> clazz, IXingYi xingYi, Object mirror) {
                            return Optional.of((Interface) new TestMultipleMultipleInterfacesImpl(mirror, xingYi));
                        }
                    });
                return Optional.empty();
            };
        }
    };


    String urlPrefix = "http://someHostAndPort";

    TelephoneNumber number = new TelephoneNumber("someNumber");
    Address address = new Address("someLine1", "someLine2");
    Person person = new Person("name", address, EmbeddedWithHasJson.value(number));
    IEntityStore<Person> personStore = IEntityStore.map(Map.of("id1", person));
    IEntityStore<Address> addressStore = IEntityStore.map(Map.of("add1", address));

    JsonTC<JsonObject> jsonTC = JsonTC.cheapJson;

    EntityRegister register = EntityRegister.simple(EntityServerCompanion.companion, PersonServerCompanion.companion, AddressServerCompanion.companion, TelephoneNumberServerCompanion.companion);
    EndPoint entityDetailsEndPoint = EntityDetailsEndpoint.entityDetailsEndPoint(jsonTC, register);
    EndPoint getPersonEndpoint = GetEntityEndpoint.getOptionalEndPoint(jsonTC, PersonServerCompanion.companion, personStore::read);
    EndPoint getAddressEndpoint = GetEntityEndpoint.getOptionalEndPoint(jsonTC, AddressServerCompanion.companion, addressStore::read);

    EndPoint composed = EndPoint.compose(getAddressEndpoint, entityDetailsEndPoint, getPersonEndpoint);
    Function<ServiceRequest, CompletableFuture<ServiceResponse>> fakeHttpClient = EndPoint.toKliesli(composed);


    XingYiClient client = XingYiClient.using(urlPrefix, fakeHttpClient,
            EntityClientCompanion.companion,
            PersonClientCompanion.companion,
            AddressClientCompanion.companion,
            testMultipleBodge);

    @Test
    public void testGetUsingUrl() throws ExecutionException, InterruptedException {
        assertEquals("/person/<id>", client.primitiveGet(IEntityUrlPattern.class, urlPrefix + "/person", e -> e.url()).get());
        assertEquals("/address/<id>", client.primitiveGet(IEntityUrlPattern.class, urlPrefix + "/address", e -> e.url()).get());
        assertEquals("[one.xingyi.restExample.IPersonAddressOps, one.xingyi.restExample.IPersonNameOps, one.xingyi.restExample.IPersonTelephoneNumberOps]", client.primitiveGet(IEntityInterfaces.class, urlPrefix + "/person", e -> e.interfaces()).get());
    }

    @Test
    public void testGetUrlPattern() throws ExecutionException, InterruptedException {
        assertEquals("/entity/<id>", client.getUrlPattern(IEntityUrlPattern.class).get());
        assertEquals("/person/<id>", client.getUrlPattern(IPersonNameOps.class).get());
        assertEquals("/address/<id>", client.getUrlPattern(IAddressLine12Ops.class).get());
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
        assertEquals("name/one.xi", client.primitiveGet(ITestMultiple.class, "http://localhost:9000/person/id1", e -> e.name() + "/" + e.address().toString().substring(0,6)).get());
        //solution to this is to have a @XingYiMulti annotation and create instance which can delegate. Actually pretty straightforwards...
    }
    @Test
    public void testWithMultipleInterfaces2() throws ExecutionException, InterruptedException {
        assertEquals("name/one.xi", client.primitiveGet(ITestMultiple.class, "http://localhost:9000/person/id1", e -> e.name() + "/" + e.address().toString().substring(0,6)).get());
        //solution to this is to have a @XingYiMulti annotation and create instance which can delegate. Actually pretty straightforwards...
    }
}
