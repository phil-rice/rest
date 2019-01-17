package one.xingyi.restAnnotations.entity;
import java.util.List;
public class EntityNotKnownException extends RuntimeException {
    public EntityNotKnownException(String entityName, List<String> legalNames) {super(entityName + " legalNames are " + legalNames);}
}
