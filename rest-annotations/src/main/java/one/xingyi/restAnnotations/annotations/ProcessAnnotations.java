package one.xingyi.restAnnotations.annotations;
import lombok.RequiredArgsConstructor;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;
import one.xingyi.restAnnotations.utils.Files;
import one.xingyi.restAnnotations.utils.WrappedException;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
@RequiredArgsConstructor
abstract class ProcessAnnotations<T extends Annotation> {
    final Class<T> annotationClass;
    final RoundEnvironment env;
    final Messager messager;
    final Filer filer;
    abstract void doit(LoggerAdapter adapter, TypeElement element, T annotation);

    public void process() {
        Set<? extends Element> elements = env.getElementsAnnotatedWith(annotationClass);
        for (Element element : elements) {
            if (element.getKind() == ElementKind.INTERFACE) {
                LoggerAdapter log = LoggerAdapter.fromMessager(messager, element);
                T annotation = element.getAnnotation(annotationClass);
                doit(log, (TypeElement) element, annotation);
            }
        }
    }

    void makeClassFile(PackageAndClassName packageAndClassName, String classString, Element element) {
        WrappedException.wrap(() -> {
            JavaFileObject builderFile = filer.createSourceFile(packageAndClassName.asString());
//            messager.printMessage(Diagnostic.Kind.NOTE, "making  " + clientImpl + "->" + builderFile.toUri());
            Files.setText(() -> new PrintWriter(builderFile.openWriter()), classString);
        });
    }

    void error(Element e, String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }

}
