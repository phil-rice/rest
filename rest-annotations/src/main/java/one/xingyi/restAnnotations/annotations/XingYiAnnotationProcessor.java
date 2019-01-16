package one.xingyi.restAnnotations.annotations;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.utils.MapUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.*;
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
        LoggerAdapter log = LoggerAdapter.fromMessager(messager);
        Set<? extends Element> xingYiElements = env.getElementsAnnotatedWith(XingYi.class);
        Set<? extends Element> xingYiopsElements = env.getElementsAnnotatedWith(XingYiOps.class);

        Map<String, List<String>> map = new HashMap<>();
        for (Element element : xingYiopsElements) {
            ProcessXingYiOpsAnnotation.findEntity(Optional.empty(), (TypeElement) element).ifPresent(entity -> MapUtils.add(map, entity, element.asType().toString()));
        }
//        log.info("Before create" + map);


        ElementsAndOps elementsAndOps = ElementsAndOps.create(names, xingYiElements, xingYiopsElements);
//        messager.printMessage(Diagnostic.Kind.NOTE, "ElementsAndOps " + elementsAndOps);
        new ProcessXingYiAnnotation(names, elementsAndOps, messager, filer, env).process();
        new ProcessXingYiOpsAnnotation(names, elementsAndOps, messager, filer, env).process();
        return false;
    }


    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    @Override public Set<String> getSupportedAnnotationTypes() {return Set.of(XingYi.class.getName(), XingYiOps.class.getName()); }
}