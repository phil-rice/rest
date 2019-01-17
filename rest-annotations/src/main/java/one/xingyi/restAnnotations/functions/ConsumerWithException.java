package one.xingyi.restAnnotations.functions;
public interface ConsumerWithException <T> {
    void apply(T t) throws Exception;
}
