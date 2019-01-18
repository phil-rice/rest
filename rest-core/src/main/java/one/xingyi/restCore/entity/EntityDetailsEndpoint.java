package one.xingyi.restcore.entity;


import one.xingyi.restAnnotations.entity.EntityDetailsRequest;
import one.xingyi.restAnnotations.entity.EntityRegister;
import one.xingyi.restAnnotations.javascript.JavascriptStore;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.endpoints.EndpointAcceptor1;
import one.xingyi.restAnnotations.utils.Digestor;
import one.xingyi.restcore.xingYiServer.EntityServerCompanion;

public interface EntityDetailsEndpoint {
    EndpointAcceptor1<EntityDetailsRequest> acceptor = EndpointAcceptor1.justOneThing("get", EntityDetailsRequest::new);

    EntityServerCompanion companion = new EntityServerCompanion();

    static <J> EndPoint entityDetailsEndPoint(JsonTC<J> jsonTC, EntityRegister entityRegister) {
        return EndPoint.javascriptAndJson(jsonTC, 200, acceptor, entityRegister, JavascriptStore.finder(Digestor.sha256,entityRegister));
    }
}