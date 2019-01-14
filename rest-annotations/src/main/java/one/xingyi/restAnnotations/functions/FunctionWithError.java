package one.xingyi.restAnnotations.functions;
public interface FunctionWithError <From,To> {
    To apply(From from) throws Exception;
}
