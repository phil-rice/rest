package one.xingyi.restAnnotations.annotations;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;

import java.util.List;
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ElementAndOps {
   public  final PackageAndClassName main;
    public final List<String> interfaces;
    public final List<String> returnedTypes;
}
