package one.xingyi.restAnnotations.annotations;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.clientside.IXingYiMultipleOps;
import one.xingyi.restAnnotations.codedom.CompositeCompanionClassCodeDom;
import one.xingyi.restAnnotations.codedom.CompositeImplClassDom;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;
import one.xingyi.restAnnotations.names.MultipleInterfaceNames;
import one.xingyi.restAnnotations.names.OpsNames;
import one.xingyi.restAnnotations.utils.Files;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.Strings;
import one.xingyi.restAnnotations.utils.WrappedException;

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
import java.util.*;
public class XingYiCompositeInterfaceAnnotationProcessor extends AbstractProcessor {
    private INames names = INames.defaultNames;
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
        LoggerAdapter log = LoggerAdapter.fromMessager(messager);
        Set<? extends Element> elements = env.getElementsAnnotatedWith(XingYiCompositeInterface.class);
        for (Element annotatedElement : elements) {
            if (annotatedElement.getKind() == ElementKind.INTERFACE) {
                TypeElement typeElement = (TypeElement) annotatedElement;
                List<String> interfaces = ListUtils.map(typeElement.getInterfaces(), e -> e.toString());
                Optional<String> opt = ListUtils.find(interfaces, s -> s.startsWith(IXingYiMultipleOps.class.getName()));

                if (opt.isEmpty())
                    log.error(annotatedElement, "This interface must extend " + IXingYiMultipleOps.class.getSimpleName());
//                XingYiCompositeInterface annotation = annotatedElement.getAnnotation(XingYiCompositeInterface.class);
                opt.ifPresent(multipleInterfaceString -> {

                    String nameOfEntity = Strings.extractFromOptionalEnvelope(IXingYiMultipleOps.class.getName(), ">", multipleInterfaceString);

                    MultipleInterfaceNames interfaceNames = new MultipleInterfaceNames(names, annotatedElement.asType().toString(), nameOfEntity);


                    CompositeImplClassDom impl = new CompositeImplClassDom(log, interfaceNames);
                    makeClassFile(interfaceNames.multipleInterfacesClientImplName, ListUtils.join(impl.createClass(), "\n"), annotatedElement);

                    List<String> cleanedInterfaces = ListUtils.filter(interfaces, i -> !i.startsWith(IXingYiMultipleOps.class.getName()));
                    CompositeCompanionClassCodeDom companionDom = new CompositeCompanionClassCodeDom(log, names, interfaceNames, cleanedInterfaces);
                    makeClassFile(interfaceNames.multipleInterfacesClientCompanion, ListUtils.join(companionDom.createClass(), "\n"), annotatedElement);
                });
            }
        }
        return false;
    }


    private void makeClassFile(PackageAndClassName packageAndClassName, String classString, Element element) {
        WrappedException.wrap(() -> {
            JavaFileObject builderFile = filer.createSourceFile(packageAndClassName.asString());
            messager.printMessage(Diagnostic.Kind.NOTE, "making  " + packageAndClassName + "->" + builderFile.toUri());
            Files.setText(() -> new PrintWriter(builderFile.openWriter()), classString);
        });
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
        set.add(XingYiCompositeInterface.class.getName());
        return set;
    }
}