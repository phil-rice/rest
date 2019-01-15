package one.xingyi.restAnnotations.annotations;

import one.xingyi.restAnnotations.names.INames;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Set;
public class XingYiAnnotationProcessor extends AbstractProcessor {
    final INames names = INames.defaultNames;

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
        new ProcessXingYiAnnotation(names, messager, filer, env).process();
        new ProcessXingYiOpsAnnotation(names, messager, filer, env).process();
        return false;
    }


    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    @Override public Set<String> getSupportedAnnotationTypes() {return Set.of(XingYi.class.getName(), XingYiOps.class.getName()); }
}