package one.xingyi.sampleServer;
import one.xingyi.restAnnotations.clientside.JavaHttpClient;
import one.xingyi.restExample.*;
import one.xingyi.restcore.xingYiServer.IEntityUrlPattern;
import one.xingyi.restAnnotations.client.Client;
import one.xingyi.restcore.xingyiclient.SimpleClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
public class SampleClient {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Client client = DomainClient.client("http://localhost:9000/", JavaHttpClient.client);
        System.out.println(client.primitiveGet(IEntityUrlPattern.class, "http://localhost:9000/person", e -> e.toString()).get());
        System.out.println(client.get(IAddressLine12.class, "add1", e -> e.toString()).get());
    }
}

