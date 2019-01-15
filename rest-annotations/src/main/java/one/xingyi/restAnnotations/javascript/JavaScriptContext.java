package one.xingyi.restAnnotations.javascript;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.clientside.IXingYiOps;

import java.util.*;
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class JavaScriptContext {
    final Set<String> visited = new HashSet<>();

    public void visited(String clazz) {
        visited.add(clazz);
    }

    public boolean hasVisited(String string) {
        return visited.contains(string);
    }
}
