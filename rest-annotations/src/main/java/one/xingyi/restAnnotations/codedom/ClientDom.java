package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.access.IEntityRead;
import one.xingyi.restAnnotations.annotations.XingYiGenerated;
import one.xingyi.restAnnotations.client.Client;
import one.xingyi.restAnnotations.clientside.IClientFactory;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.endpoints.EndpointAcceptor0;
import one.xingyi.restAnnotations.entity.EntityRegister;
import one.xingyi.restAnnotations.http.ServiceRequest;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public class ClientDom {
    final EntityNames clientEntityName;
    final List<EntityNames> entityNames;
    public ClientDom(EntityNames clientEntityName, List<EntityNames> entityNames) {
        this.clientEntityName = clientEntityName;
        this.entityNames = entityNames;
    }

    public List<String> createClass() {
        List<String> result = new ArrayList<>();
        result.add("package " + clientEntityName.entityInterface.packageName + ";");
        result.add("");
        result.add("import " + EndPoint.class.getName() + ";");
        result.add("import " + JsonTC.class.getName() + ";");
        result.add("import " + Client.class.getName() + ";");
        result.add("import " + Function.class.getName() + ";");
        result.add("import " + CompletableFuture.class.getName() + ";");
        result.add("import " + ServiceRequest.class.getName() + ";");
        result.add("import " + ServiceResponse.class.getName() + ";");
        result.add("import " + IClientFactory.class.getName() + ";");
        result.add("import " + Arrays.class.getName() + ";");
        result.add("import " + List.class.getName() + ";");
        result.add("import " + ListUtils.class.getName() + ";");
        result.add("import " + XingYiGenerated.class.getName() + ";");
        result.add("import one.xingyi.restcore.entity.SimpleEntityRegister;");
        result.add("import one.xingyi.restcore.entity.EntityDetailsEndpoint;");
        result.add("import one.xingyi.restcore.access.GetEntityEndpoint;");
        result.add("import one.xingyi.restcore.xingYiServer.EntityServerCompanion;");
        result.add("import one.xingyi.restcore.xingYiServer.EntityClientCompanion;");

        result.add("public class " + clientEntityName.serverImplementation.className + " implements " + clientEntityName.entityInterface.asString() + "{");
        result.addAll(Formating.indent(createCreate()));
        result.add("}");
        return result;
    }


    public List<String> createCreate() {
        List<String> result = new ArrayList<>();
        result.add("@XingYiGenerated");
        result.add("public static Client client(String urlPrefix, Function< ServiceRequest, CompletableFuture<ServiceResponse>> httpClient, IClientFactory...others){");
        result.add(Formating.indent + "List<IClientFactory> entityFactories = Arrays.asList( EntityClientCompanion.companion," + ListUtils.mapJoin(entityNames, ",", en -> en.clientCompanion.asString() + ".companion") + ");");
        result.add(Formating.indent + "List<IClientFactory> factories = ListUtils.append(entityFactories, Arrays.asList(others));");
        result.add(Formating.indent + "return one.xingyi.restcore.xingyiclient.SimpleClient.using(urlPrefix, httpClient, factories);");
        result.add("}");
        return result;
//                    EntityClientCompanion.companion,
//                    PersonClientCompanion.companion,
//                    AddressClientCompanion.companion,
//                    TestMultipleClientCompanion.companion); //TODO Perhaps this can be automated?
    }

}

