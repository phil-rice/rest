package one.xingyi.restAnnotations.annotations;

import one.xingyi.restAnnotations.utils.Files;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.Strings;
import one.xingyi.restAnnotations.utils.WrappedException;
import one.xingyi.restAnnotations.codedom.ClassDom;
import one.xingyi.restAnnotations.codedom.FieldList;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
public class XingYiAnnotationProcessor extends AbstractProcessor {


    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        processingEnv.getOptions();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annoations, RoundEnvironment env) {
//        XingYiField.<Object, Integer>create(p -> p.hashCode(), (p, h) -> p);
        for (Element annotatedElement : env.getElementsAnnotatedWith(XingYi.class)) {
            if (annotatedElement.getKind() == ElementKind.INTERFACE) {
                FieldList fields = FieldList.create(annotatedElement.getEnclosedElements());
                String interfaceName = annotatedElement.getSimpleName().toString();
                String packageName = Strings.allButLastSegment(".", annotatedElement.asType().toString());
                ClassDom dom = new ClassDom(packageName, interfaceName, fields);
                String classString = ListUtils.join(dom.createClass(), "\n");
//                LensDom lensDom = new LensDom()
                WrappedException.wrap(() -> {
                    JavaFileObject builderFile = filer.createSourceFile(packageName + "." + dom.className);
//                    error(annotatedElement, "We are here @%s", builderFile.getName());
                    Files.setText(() -> new PrintWriter(builderFile.openWriter()), "// " + annotatedElement.asType() + "\n//" + fields + "\n\n" + classString);
                });
            }
        }
        return false;
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }
    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    @Override public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(XingYi.class.getName());
        return set;
    }
}