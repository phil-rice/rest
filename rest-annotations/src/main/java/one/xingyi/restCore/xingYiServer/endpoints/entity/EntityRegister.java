package one.xingyi.restCore.xingYiServer.endpoints.entity;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.entity.Companion;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface EntityRegister extends Function<EntityDetailsRequest, CompletableFuture<EntityDetailsResponse>> {

    static EntityRegister register(List<EntityRegistrationDetails> details) {
        return new SimpleEntityRegister(details);
    }
    static EntityRegister simple(Companion<?, ?>... companions) {
        return new SimpleEntityRegister(ListUtils.map(Arrays.asList(companions),
                c -> new EntityRegistrationDetails(c.entityName(), "/" + c.entityName() + "/<id>", c)));
    }
}

@ToString
@EqualsAndHashCode
class SimpleEntityRegister implements EntityRegister {

    final Map<String, EntityRegistrationDetails> register;
    SimpleEntityRegister(List<EntityRegistrationDetails> details) {
        this.register = ListUtils.foldLeft(new LinkedHashMap<>(), details, (acc, rd) -> {
            acc.put(rd.entityName.toLowerCase(), rd);
            return acc;
        });

    }

    @Override public CompletableFuture<EntityDetailsResponse> apply(EntityDetailsRequest entityDetailsRequest) {
        EntityRegistrationDetails details = register.get(entityDetailsRequest.entityName);
        if (details == null)
            throw new EntityNotKnownException(entityDetailsRequest.entityName, Arrays.asList(register.keySet().toArray(new String[0])));
        return CompletableFuture.completedFuture(new EntityDetailsResponse(details));
    }
}
