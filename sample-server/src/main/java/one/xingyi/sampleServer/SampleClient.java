package one.xingyi.sampleServer;
import one.xingyi.restAnnotations.clientside.JavaHttpClient;
import one.xingyi.restExample.PersonClientCompanion;
import one.xingyi.restcore.xingYiServer.IEntityUrlPattern;
import one.xingyi.restcore.xingyiclient.XingYiClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
public class SampleClient {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        XingYiClient client = XingYiClient.using("http://localhost:9000/",JavaHttpClient.client, new PersonClientCompanion());
        CompletableFuture<String> response = client.primitiveGet(IEntityUrlPattern.class, "localhost:9000/person", e -> e.toString());
        System.out.println(response.get());
    }
}

