package one.xingyi.restcore.entity;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.entity.Companion;
import one.xingyi.restAnnotations.utils.Files;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restcore.xingYiServer.Entity;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface EntityRegister extends Function<EntityDetailsRequest, CompletableFuture<Entity>> {

    String javascript();

    static EntityRegister register(List<EntityRegistrationDetails> details) {
        return new SimpleEntityRegister(details);
    }
    static EntityRegister simple(Companion<?, ?>... companions) {
        return new SimpleEntityRegister(ListUtils.map(Arrays.asList(companions),
                c -> new EntityRegistrationDetails(c.entityName(), "<host>/" + c.entityName().toLowerCase() + "/<id>", c)));
    }
}

@ToString
@EqualsAndHashCode
class SimpleEntityRegister implements EntityRegister {

    final Map<String, EntityRegistrationDetails> register;
    final String javascript;

    SimpleEntityRegister(List<EntityRegistrationDetails> details) {
        this.register = ListUtils.foldLeft(new LinkedHashMap<>(), details, (acc, rd) -> {
            acc.put(rd.entityName.toLowerCase(), rd);
            return acc;
        });
        this.javascript = Files.getText("header.js") + ListUtils.aggLeft(new StringBuilder(), details, (acc, d) -> acc.append(d.companion.javascript() + "\n")).toString();
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
    @Override public String javascript() {
        return javascript;
    }
}
