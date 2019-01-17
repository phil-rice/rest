package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.access.IEntityRead;
import one.xingyi.restAnnotations.endpoints.EndPoint;
import one.xingyi.restAnnotations.endpoints.EndpointAcceptor0;
import one.xingyi.restAnnotations.entity.EntityRegister;
import one.xingyi.restAnnotations.http.ServiceResponse;
import one.xingyi.restAnnotations.marshelling.JsonTC;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
public class ServerDom {
    final EntityNames serverEntityName;
    final List<EntityNames> entityNames;
    final List<EntityNames> exposedEntityNames;
    private final String joining;
    public ServerDom(EntityNames serverEntityName, List<EntityDetails> entityNames) {
        this.serverEntityName = serverEntityName;
        this.entityNames = ListUtils.map(entityNames, e -> e.entityNames);
        this.exposedEntityNames = ListUtils.map(ListUtils.filter(entityNames, e -> e.hasUrlPattern), e -> e.entityNames);
        joining = exposedEntityNames.size() > 0 ? "," : "";
    }

    public List<String> createClass() {
        List<String> result = new ArrayList<>();
        result.add("package " + serverEntityName.entityInterface.packageName + ";");
        result.add("");
        result.add("import " + EndPoint.class.getName() + ";");
        result.add("import " + JsonTC.class.getName() + ";");
        result.add("import " + IEntityRead.class.getName() + ";");
        result.add("import " + EntityRegister.class.getName() + ";");
        result.add("import " + EndpointAcceptor0.class.getName() + ";");
        result.add("import " + ServiceResponse.class.getName() + ";");
        result.add("import one.xingyi.restcore.entity.SimpleEntityRegister;");
        result.add("import one.xingyi.restcore.entity.EntityDetailsEndpoint;");
        result.add("import one.xingyi.restcore.access.GetEntityEndpoint;");
        result.add("import one.xingyi.restcore.xingYiServer.EntityServerCompanion;");

        result.add("public class " + serverEntityName.serverImplementation.className + " implements " + serverEntityName.entityInterface.asString() + "{");
        result.addAll(Formating.indent(createCreateServerMethod()));
        result.add("");
        result.addAll(Formating.indent(createOtherEndPoints()));
        result.add("}");
        return result;
    }
    //        //All the XingYis appear here
    //        EntityRegister register = EntityRegister.simple(
    //                EntityServerCompanion.companion,
    //                PersonServerCompanion.companion,
    //                AddressServerCompanion.companion,
    //                TelephoneNumberServerCompanion.companion);
    //
    //        //This serves the bookmarked urls used by the entities
    //        EndPoint entityDetailsEndPoint = EntityDetailsEndpoint.entityDetailsEndPoint(jsonTC, register);
    //        EndPoint getPersonEndpoint = GetEntityEndpoint.getOptionalEndPoint(jsonTC, register, PersonServerCompanion.companion, personRead::read);
    //        EndPoint getAddressEndpoint = GetEntityEndpoint.getOptionalEndPoint(jsonTC, register, AddressServerCompanion.companion, addressStore::read);
    //        return EndPoint.compose(index, keepalive, entityDetailsEndPoint, getAddressEndpoint, getPersonEndpoint, getAddressEndpoint);

    String createParameters() {
        return ListUtils.mapJoin(exposedEntityNames, ",", en -> "IEntityRead<" + en.serverImplementation.className + "> " + en.serverImplementation.className.toLowerCase());
    }

    List<String> createCreateServerMethod() {
        String register = ListUtils.mapJoin(entityNames, ",\n" + Formating.indent + Formating.indent, en -> en.serverCompanion.asString() + ".companion") + ");";

        List<String> result = new ArrayList<>();
        result.add("//anything with urlPattern set in it should appear as a parameter");
        result.add("//If you have a compilation error check you've done a full compile: there is an issue with the annotation processors");
        result.add("public static <J> EndPoint createEndpoints(JsonTC<J> jsonTC" + joining + createParameters() + ") {");
        result.add(Formating.indent + "EntityRegister register = SimpleEntityRegister.simple(EntityServerCompanion.companion, ");
        result.add(Formating.indent + register);
        result.add(Formating.indent + "EndPoint entityDetailsEndPoint = EntityDetailsEndpoint.entityDetailsEndPoint(jsonTC, register);");
        result.addAll(Formating.indent(ListUtils.map(exposedEntityNames, en -> "EndPoint get" + en.serverImplementation.className + "Endpoint = GetEntityEndpoint.getOptionalEndPoint(jsonTC, register, " + en.serverCompanion.asString() + ".companion, " + en.serverImplementation.className.toLowerCase() + "::read);")));
        result.add(Formating.indent + "return EndPoint.compose(entityDetailsEndPoint" + joining + ListUtils.mapJoin(exposedEntityNames, ",", en -> "get" + en.serverImplementation.className + "Endpoint") + ");");
        result.add("}");
        return result;
    }

    List<String> createOtherEndPoints() {
        List<String> result = new ArrayList<>();
        result.add("public static EndPoint index = EndPoint.function(EndpointAcceptor0.exact(\"get\", \"/\"), sr -> ServiceResponse.html(200, \"made it: you sent\" + sr));");
        result.add("public static EndPoint keepalive = EndPoint.staticEndpoint(EndpointAcceptor0.exact(\"get\", \"/keepalive\"), ServiceResponse.html(200, \"Alive\"));");
        result.add("public static <J>EndPoint createWithHelpers(JsonTC<J> jsonTC" + joining + createParameters() + "){");
        result.add(Formating.indent + "return EndPoint.printlnLog(EndPoint.compose(index, keepalive, createEndpoints(jsonTC" + joining +
                ListUtils.mapJoin(exposedEntityNames, ",", en -> en.serverImplementation.className.toLowerCase()) + ")));");
        result.add("}");
        return result;
        //    EndPoint index = EndPoint.function(EndpointAcceptor0.exact("get", "/"), sr -> ServiceResponse.html(200, "made it: you sent" + sr));
//    EndPoint keepalive = EndPoint.staticEndpoint(EndpointAcceptor0.exact("get", "/keepalive"), ServiceResponse.html(200, "Alive"));
//    EndPoint all = EndPoint.printlnLog(EndPoint.compose(index, keepalive, entityEndpoints));


    }
}
