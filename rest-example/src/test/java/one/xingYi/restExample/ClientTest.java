package one.xingYi.restExample;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.marshelling.JsonObject;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restExample.*;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restcore.entity.EntityDetailsEndpoint;
import one.xingyi.restcore.entity.EntityRegister;
import one.xingyi.restcore.xingYiServer.EntityClientCompanion;
import one.xingyi.restcore.xingYiServer.EntityServerCompanion;
import one.xingyi.restcore.xingYiServer.IEntityInterfaces;
import one.xingyi.restcore.xingYiServer.IEntityUrlPattern;
import one.xingyi.restcore.xingyiclient.XingYiClient;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
public class ClientTest {

    JsonTC<JsonObject> jsonTC = JsonTC.cheapJson;
    EntityRegister register = EntityRegister.simple(EntityServerCompanion.companion, PersonServerCompanion.companion, AddressServerCompanion.companion, TelephoneNumberServerCompanion.companion);
    EndPoint entityDetailsEndPoint = EntityDetailsEndpoint.entityDetailsEndPoint(jsonTC, register);
    Function<ServiceRequest, CompletableFuture<ServiceResponse>> fakeHttpClient = EndPoint.toKliesli(entityDetailsEndPoint);
    XingYiClient client = XingYiClient.using(fakeHttpClient, new EntityClientCompanion());

    @Test
    public void testGetUsingUrl() throws ExecutionException, InterruptedException {
        assertEquals("/person/<id>", client.getFromUrl(IEntityUrlPattern.class, "localhost:9000/person", e -> e.url()).get());
        assertEquals("/address/<id>", client.getFromUrl(IEntityUrlPattern.class, "localhost:9000/address", e -> e.url()).get());
        assertEquals("[one.xingyi.restExample.IPersonAddressOps, one.xingyi.restExample.IPersonNameOps, one.xingyi.restExample.IPersonTelephoneNumberOps]", client.getFromUrl(IEntityInterfaces.class, "localhost:9000/person", e -> e.interfaces()).get());

    }

    interface TestMultiple extends IEntityUrlPattern, IEntityInterfaces {}

    @Test
    public void testWithMultipleInterfaces() throws ExecutionException, InterruptedException {
        assertEquals("", client.getFromUrl(TestMultiple.class, "localhost:9000/person", e -> e.interfaces()).get());
        //solution to this is to have a @XingYiMulti annotation and create instance which can delegate. Actually pretty straightforwards...
    }
}
