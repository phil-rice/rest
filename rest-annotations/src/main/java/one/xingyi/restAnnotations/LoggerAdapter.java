package one.xingyi.restAnnotations;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
public interface LoggerAdapter {
    void info(String message);
    static LoggerAdapter fromMessager(Messager messager, Element element) {
        return msg -> messager.printMessage(Diagnostic.Kind.NOTE, msg, element);
    }
}
