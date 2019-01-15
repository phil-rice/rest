package one.xingyi.restAnnotations.entity;
import java.util.List;
public interface IOpsServerCompanion {
    Companion entityCompanion();
    List<Companion<?, ?>> returnTypes();
}
