package one.xingyi.restAnnotations.annotations;

import com.sun.jdi.InterfaceType;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.codedom.CompositeInterfaceClassCodeDom;
import one.xingyi.restAnnotations.codedom.FieldList;
import one.xingyi.restAnnotations.codedom.INames;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;
import one.xingyi.restAnnotations.utils.Files;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.WrappedException;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.lang.reflect.Array;
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
        Set<? extends Element> elements = env.getElementsAnnotatedWith(XingYiCompositeInterface.class);
        for (Element annotatedElement : elements) {
            if (annotatedElement.getKind() == ElementKind.INTERFACE) {
                PackageAndClassName entityName = names.get(annotatedElement);
                PackageAndClassName implementationname = names.clientMultipleInterfacesName(entityName);
                PackageAndClassName clientName = names.clientImplName(entityName);
                LoggerAdapter log = LoggerAdapter.fromMessager(messager, annotatedElement);

                XingYiCompositeInterface annotation = annotatedElement.getAnnotation(XingYiCompositeInterface.class);
                String companionClassName = annotation.value();
//                List<List<Name>> interfaceNames = ListUtils.map(typeElement.getInterfaces(), i -> ListUtils.map(((TypeElement) i).getEnclosedElements(), e -> e.getSimpleName()));

                FieldList fields = FieldList.create(log, names, entityName.className, annotatedElement.getEnclosedElements());
                List<String> strings = new CompositeInterfaceClassCodeDom(log, names, entityName.className,implementationname, companionClassName, Arrays.asList()).createClass();
                makeClassFile(implementationname, ListUtils.join(strings, "\n"), annotatedElement);
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