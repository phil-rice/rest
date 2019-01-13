package one.xingyi.restAnnotations.utils;
public interface ConsumerWithException<T> {
    void accept(T t) throws Exception;
}
