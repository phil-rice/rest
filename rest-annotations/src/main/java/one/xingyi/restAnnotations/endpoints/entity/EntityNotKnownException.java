package one.xingyi.restAnnotations.endpoints.entity;
public class EntityNotKnownException extends RuntimeException {
    public EntityNotKnownException(String entityName) {super(entityName);}
}
