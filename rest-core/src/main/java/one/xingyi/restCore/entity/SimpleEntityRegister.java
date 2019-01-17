package one.xingyi.restcore.entity;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.entity.*;
import one.xingyi.restAnnotations.utils.Files;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.MapUtils;
import one.xingyi.restcore.xingYiServer.Entity;

import java.util.*;
import java.util.concurrent.CompletableFuture;
@ToString
@EqualsAndHashCode
public class SimpleEntityRegister implements EntityRegister<Entity> {
    private final Map<String, String> javascriptMap;
    public static EntityRegister register(List<EntityRegistrationDetails> details) { return new SimpleEntityRegister(details); }

    public static EntityRegister simple(Companion<?, ?>... companions) {
        return new SimpleEntityRegister(ListUtils.map(Arrays.asList(companions),
                c -> new EntityRegistrationDetails(c.entityName(), "<host>/" + c.entityName().toLowerCase() + "/<id>", c)));
    }


    final Map<String, EntityRegistrationDetails> register;
    final String javascript;

    SimpleEntityRegister(List<EntityRegistrationDetails> details) {
        this.register = ListUtils.foldLeft(new LinkedHashMap<>(), details, (acc, rd) -> {
            acc.put(rd.entityName.toLowerCase(), rd);
            return acc;
        });
        String rootJavascript = Files.getText("header.js");
        this.javascript = rootJavascript + ListUtils.aggLeft(new StringBuilder(), details, (acc, d) -> acc.append(d.companion.javascript() + "\n")).toString();
        this.javascriptMap = MapUtils.append(ListUtils.add(ListUtils.map(List.copyOf(register.values()), r -> r.companion.javascriptMap()), Map.of("root", rootJavascript)));

    }

    @Override public CompletableFuture<Entity> apply(EntityDetailsRequest entityDetailsRequest) {
        EntityRegistrationDetails details = register.get(entityDetailsRequest.entityName);
        if (details == null)
            throw new EntityNotKnownException(entityDetailsRequest.entityName, Arrays.asList(register.keySet().toArray(new String[0])));
        List<Class> classes = Arrays.asList(details.companion.supported().toArray(new Class[0]));
        List<String> supported = ListUtils.map(classes, Class::getName);
        Collections.sort(supported);
        return CompletableFuture.completedFuture(new Entity(details.urlPattern, supported.toString()));
    }
    @Override public String javascriptFor(String lens) {
        return Optional.ofNullable(javascriptMap.get(lens)).orElseThrow(()-> new RuntimeException("Asked for lens '"+ lens + "' legal values are " + register.keySet() ));
    }
    @Override public String javascript() {
        return javascript;
    }
}
