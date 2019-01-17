package one.xingyi.sampleServer;
import one.xingyi.restAnnotations.access.IEntityStore;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.entity.EmbeddedWithHasJson;
import one.xingyi.restAnnotations.marshelling.JsonObject;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.server.EndpointHandler;
import one.xingyi.restAnnotations.server.HttpUtils;
import one.xingyi.restAnnotations.server.SimpleServer;
import one.xingyi.restExample.*;

import java.util.Map;



public class SampleServer implements MockPersonAndAddressStores {

    public static void main(String[] args) {
        JsonTC<JsonObject> jsonTC = JsonTC.cheapJson;
        EndPoint endPoints = PersonServer.createWithHelpers(jsonTC, addressStore, personStore);//the stores are  just ways of 'given an id go get a completable future of xxx' a
        SimpleServer server = new SimpleServer(HttpUtils.makeDefaultExecutor(), new EndpointHandler(endPoints), 9000);
        server.start();
    }
}
