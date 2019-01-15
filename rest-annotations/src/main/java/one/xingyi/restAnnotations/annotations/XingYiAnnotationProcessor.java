package one.xingyi.restAnnotations.annotations;

import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.codedom.*;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
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
                EntityNames entityNames = new EntityNames(names, annotatedElement.asType().toString());

                LoggerAdapter log = LoggerAdapter.fromMessager(messager, annotatedElement);
                FieldList fields = FieldList.create(log, names, entityNames.entityInterface.className, annotatedElement.getEnclosedElements());
                List<String> errors = names.validateEntityName(entityNames.entityInterface);
                if (errors.size() > 0) error(annotatedElement, errors.toString());
                else {
                    XingYi annotation = annotatedElement.getAnnotation(XingYi.class);
                    BookmarkAndUrlPattern bookmarkAndUrlPattern = new BookmarkAndUrlPattern(entityNames.serverImplementation.className, annotation.bookmarked(), annotation.urlPattern());

                    EntityOnServerClassDom classDom = new EntityOnServerClassDom(log, names, entityNames, fields);
                    makeClassFile(classDom.packageAndClassName, ListUtils.join(classDom.createClass(), "\n"), annotatedElement);

                    EntityOnClientClassDom clientDom = new EntityOnClientClassDom(log, names, entityNames, fields);
                    makeClassFile(clientDom.packageAndClassName, ListUtils.join(clientDom.createClass(), "\n"), annotatedElement);

                    CompanionOnServerClassDom companionOnServerClassDom = new CompanionOnServerClassDom(names, entityNames, fields, bookmarkAndUrlPattern);
                    makeClassFile(companionOnServerClassDom.companionName, ListUtils.join(companionOnServerClassDom.createClass(), "\n"), annotatedElement);

                    CompanionOnClientClassDom companionOnClientClassDom = new CompanionOnClientClassDom(log, names, entityNames, fields, bookmarkAndUrlPattern);
                    makeClassFile(companionOnClientClassDom.companionName, ListUtils.join(companionOnClientClassDom.createClass(), "\n"), annotatedElement);

                    for (OpsInterfaceClassDom dom : classDom.nested()) {
                        makeClassFile(dom.opsName, ListUtils.join(dom.createClass(), "\n"), annotatedElement);
                    }
                }
            }
        }
        return false;
    }


    private void makeClassFile(PackageAndClassName packageAndClassName, String classString, Element element) {
        WrappedException.wrap(() -> {
            JavaFileObject builderFile = filer.createSourceFile(packageAndClassName.asString());
//            messager.printMessage(Diagnostic.Kind.NOTE, "making  " + packageAndClassName + "->" + builderFile.toUri());
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