package one.xingyi.sampleServer;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.server.EndpointHandler;
import one.xingyi.restAnnotations.server.HttpUtils;
import one.xingyi.restAnnotations.server.SimpleServer;
import one.xingyi.restExample.AddressServerCompanion;
import one.xingyi.restExample.PersonServerCompanion;
import one.xingyi.restExample.TelephoneNumberServerCompanion;
import one.xingyi.restcore.endpoints.EndPoint;
import one.xingyi.restcore.endpoints.EndpointAcceptor0;
import one.xingyi.restcore.endpoints.entity.EntityDetailsEndpoint;
import one.xingyi.restcore.endpoints.entity.EntityRegister;

public class SampleServer {

    public static void main(String[] args) {
        EntityRegister register = EntityRegister.simple(PersonServerCompanion.companion, AddressServerCompanion.companion, TelephoneNumberServerCompanion.companion);

        EndPoint index = EndPoint.function(EndpointAcceptor0.exact("get", "/"),
                sr -> ServiceResponse.html(200, "made it: you sent" + sr));

        EndPoint keepalive = EndPoint.staticEndpoint(EndpointAcceptor0.exact("get", "/keepalive"), ServiceResponse.html(200, "Alive"));

        EndPoint entityDetailsEndPoint = EntityDetailsEndpoint.entityDetailsEndPoint(register);

        EndPoint all = EndPoint.printlnLog(EndPoint.compose(index, keepalive, entityDetailsEndPoint));
        SimpleServer server = new SimpleServer(HttpUtils.makeDefaultExecutor(), new EndpointHandler(all), 9000);
        server.start();
    }
}
