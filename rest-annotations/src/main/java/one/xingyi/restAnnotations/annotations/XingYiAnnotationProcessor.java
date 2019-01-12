package one.xingyi.restAnnotations.annotations;

import one.xingyi.restAnnotations.codedom.*;
import one.xingyi.restAnnotations.utils.Files;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.WrappedException;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.*;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class XingYiAnnotationProcessor extends AbstractProcessor {

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
        Set<? extends Element> elements = env.getElementsAnnotatedWith(XingYi.class);
        for (Element annotatedElement : elements) {
            if (annotatedElement.getKind() == ElementKind.INTERFACE) {
                PackageAndClassName entityName = names.get(annotatedElement);
                FieldList fields = FieldList.create(entityName.className, annotatedElement.getEnclosedElements());
                List<String> errors = names.validateEntityName(entityName);
                if (errors.size() > 0) error(annotatedElement, errors.toString());
                else {
                    EntityOnServerClassDom classDom = new EntityOnServerClassDom(names, names.serverImplName(entityName), names.interfaceName(entityName), fields);
                    makeClassFile(classDom.packageAndClassName, ListUtils.join(classDom.createClass(), "\n"));
                    EntityOnClientClassDom clientDom = new EntityOnClientClassDom(names, names.clientImplName(entityName), names.interfaceName(entityName), fields);
                    makeClassFile(clientDom.packageAndClassName, ListUtils.join(clientDom.createClass(), "\n"));
                    for (OpsInterfaceClassDom dom : classDom.nested()) {
                        messager.printMessage(Diagnostic.Kind.NOTE, "making interfacedom " + dom.opsName.asString());
                        makeClassFile(dom.opsName, ListUtils.join(dom.createClass(), "\n"));
                    }
                }
            }
        }
        return false;
    }


    private void makeClassFile(PackageAndClassName packageAndClassName, String classString) {
        WrappedException.wrap(() -> {
            JavaFileObject builderFile = filer.createSourceFile(packageAndClassName.asString());
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
        set.add(XingYi.class.getName());
        return set;
    }
}