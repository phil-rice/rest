package one.xingyi.restAnnotations.endpoints.entity;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import one.xingyi.restAnnotations.utils.ListUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
public interface EntityRegister extends Function<EntityDetailsRequest, CompletableFuture<EntityDetailsResponse>> {

    static EntityRegister simple(List<EntityRegistrationDetails> details) {
        return new SimpleEntityRegister(details);
    }
}

@ToString
@EqualsAndHashCode
class SimpleEntityRegister implements EntityRegister {

    final Map<String, EntityRegistrationDetails> register;
    SimpleEntityRegister(List<EntityRegistrationDetails> details) {
        this.register = ListUtils.foldLeft(new HashMap<>(), details, (acc, rd) -> {
            acc.put(rd.entityName, rd);
            return acc;
        });
    }

    @Override public CompletableFuture<EntityDetailsResponse> apply(EntityDetailsRequest entityDetailsRequest) {
        EntityRegistrationDetails details = register.get(entityDetailsRequest.entityName);
        if (details == null)
            throw new EntityNotKnownException(entityDetailsRequest.entityName);
        return CompletableFuture.completedFuture(new EntityDetailsResponse(details));
    }
}
