package one.xingyi.restAnnotations.entity;
import one.xingyi.restAnnotations.clientside.IClientCompanion;

import java.util.List;
public interface IOpsClientCompanion {
    IClientCompanion entityCompanion();
    List<String> lensNames();
}
