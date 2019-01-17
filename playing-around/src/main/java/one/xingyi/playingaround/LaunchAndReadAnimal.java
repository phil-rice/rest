package one.xingyi.playingaround;
import one.xingyi.restAnnotations.access.IEntityStore;
import one.xingyi.restAnnotations.client.Client;
import one.xingyi.restAnnotations.clientside.JavaHttpClient;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.marshelling.JsonObject;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.server.SimpleServer;

import java.util.Map;
public class LaunchAndReadAnimal {
    static Animal animal = new Animal("someAnimal name");

    static IEntityStore<Animal> animalStore = IEntityStore.map(Map.of("id1", animal));

    static int port = 9000;
    public static JsonTC<JsonObject> jsonTC = JsonTC.cheapJson;
    public static EndPoint endPoints = PlayingServer.createEndpoints(jsonTC, animalStore);

    public static void main(String[] args) {
        Client client = PlayingClient.client("http://localhost:" + port, JavaHttpClient.client);
        SimpleServer.doAndThenStop(port, endPoints, server -> {
            System.out.println(client.get(IAnimalName.class, "id1", e -> e.name() ).get());
//            System.out.println(client.get(IAnimalName.class, "id1", e -> e.name() ).get());
        });
    }
}
