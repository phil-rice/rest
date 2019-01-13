package one.xingYi.restExample;
import one.xingyi.restAnnotations.clientside.JavaHttpClient;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.marshelling.JsonObject;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restExample.AddressServerCompanion;
import one.xingyi.restExample.PersonClientCompanion;
import one.xingyi.restExample.PersonServerCompanion;
import one.xingyi.restExample.TelephoneNumberServerCompanion;
import one.xingyi.restcore.endpoints.EndPoint;
import one.xingyi.restcore.endpoints.entity.EntityDetailsEndpoint;
import one.xingyi.restcore.endpoints.entity.EntityRegister;
import one.xingyi.restcore.xingYiServer.EntityClientCompanion;
import one.xingyi.restcore.xingYiServer.EntityServerCompanion;
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
    Function<ServiceRequest, CompletableFuture<ServiceResponse>> fakeHttpClient = EndPoint.withoutOptional(entityDetailsEndPoint);
    XingYiClient client = XingYiClient.using(fakeHttpClient, new EntityClientCompanion());

    @Test
    public void testGetUsingUrl() throws ExecutionException, InterruptedException {
        assertEquals("/person/<id>", client.getFromUrl(IEntityUrlPattern.class, "localhost:9000/person", e -> e.url()).get());
        assertEquals("/address/<id>", client.getFromUrl(IEntityUrlPattern.class, "localhost:9000/address", e -> e.url()).get());

    }
}
