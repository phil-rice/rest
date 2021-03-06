package one.xingyi.restAnnotations.entity;
import one.xingyi.restAnnotations.javascript.JavascriptFor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
//This E is 'Entity'. In order to 'eat our own dogfood' this needed to be separated from E
public interface EntityRegister<E> extends Function<EntityDetailsRequest, CompletableFuture<E>> , JavascriptFor {


}

