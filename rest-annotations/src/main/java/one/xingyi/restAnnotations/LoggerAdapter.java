package one.xingyi.restAnnotations;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
public interface LoggerAdapter {
    void info(String message);
    void warning(Element element, String message);
    void error(Element element, String message);
    static LoggerAdapter fromMessager(Messager messager, Element element) {
        return new LoggerAdapter() {
            @Override public void info(String message) {
                messager.printMessage(Diagnostic.Kind.NOTE, message, element);
            }
            @Override public void warning(Element element, String message) {
                messager.printMessage(Diagnostic.Kind.WARNING, message, element);
            }
            @Override public void error(Element element, String message) {
                messager.printMessage(Diagnostic.Kind.ERROR, message, element);
            }
        };
    }
}
