package one.xingyi.restAnnotations.annotations;

import lombok.RequiredArgsConstructor;
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
        new ProcessXingYiAnnotation(messager, filer, names, XingYi.class, env).process();
        return false;
    }

    @RequiredArgsConstructor
    static abstract class ProcessAnnotations {
        final Class annotationClass;
        final RoundEnvironment env;
        final Messager messager;
        final Filer filer;
        abstract void doit(LoggerAdapter adapter, Element element, XingYi annotation);

        public void process() {
            Set<? extends Element> elements = env.getElementsAnnotatedWith(annotationClass);
            for (Element element : elements) {
                if (element.getKind() == ElementKind.INTERFACE) {
                    LoggerAdapter log = LoggerAdapter.fromMessager(messager, element);
                    XingYi annotation = element.getAnnotation(XingYi.class);
                    doit(log, element, annotation);
                }
            }
        }

        void makeClassFile(PackageAndClassName packageAndClassName, String classString, Element element) {
            WrappedException.wrap(() -> {
                JavaFileObject builderFile = filer.createSourceFile(packageAndClassName.asString());
//            messager.printMessage(Diagnostic.Kind.NOTE, "making  " + packageAndClassName + "->" + builderFile.toUri());
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

    static class ProcessXingYiAnnotation extends ProcessAnnotations {
        private final INames names;
        final ElementsAndOps elementsAndOps;

        public ProcessXingYiAnnotation(Messager messager, Filer filer, INames names, Class annotationClass, RoundEnvironment env) {
            super(annotationClass, env, messager, filer);
            this.names = names;
            Set<? extends Element> elements = env.getElementsAnnotatedWith(XingYi.class);
            this.elementsAndOps = ElementsAndOps.create(elements);
        }

        @Override void doit(LoggerAdapter log, Element annotatedElement, XingYi annotation) {
            EntityNames entityNames = new EntityNames(names, annotatedElement.asType().toString());
            FieldList fields = FieldList.create(log, names, elementsAndOps, entityNames.entityInterface.className, annotatedElement.getEnclosedElements());
            List<String> errors = names.validateEntityName(entityNames.entityInterface);
            if (errors.size() > 0) error(annotatedElement, errors.toString());
            else {
                BookmarkAndUrlPattern bookmarkAndUrlPattern = new BookmarkAndUrlPattern(entityNames.serverImplementation.className, annotation.bookmarked(), annotation.urlPattern());
                EntityOnServerClassDom classDom = new EntityOnServerClassDom(log, names, entityNames, fields);
                for (OpsInterfaceClassDom dom : classDom.nestedOps()) { //needs to be earlier as this makes classes other use
                    makeClassFile(dom.opsName, ListUtils.join(dom.createClass(), "\n"), annotatedElement);
                }
                for (OpsServerCompanionClassDom dom : classDom.nestedOpServerCompanions()) { //needs to be earlier as this makes classes other use
                    makeClassFile(dom.companionName, ListUtils.join(dom.createClass(), "\n"), annotatedElement);
                }
                for (OpsClientCompanionClassDom dom : classDom.nestedOpClientCompanions()) { //needs to be earlier as this makes classes other use
                    makeClassFile(dom.companionName, ListUtils.join(dom.createClass(), "\n"), annotatedElement);
                }
                makeClassFile(classDom.packageAndClassName, ListUtils.join(classDom.createClass(), "\n"), annotatedElement);
                EntityOnClientClassDom clientDom = new EntityOnClientClassDom(log, names, entityNames, fields);
                makeClassFile(clientDom.packageAndClassName, ListUtils.join(clientDom.createClass(), "\n"), annotatedElement);


                CompanionOnServerClassDom companionOnServerClassDom = new CompanionOnServerClassDom(names, entityNames, fields, bookmarkAndUrlPattern);
                makeClassFile(companionOnServerClassDom.companionName, ListUtils.join(companionOnServerClassDom.createClass(), "\n"), annotatedElement);

                CompanionOnClientClassDom companionOnClientClassDom = new CompanionOnClientClassDom(log, names, entityNames, fields, bookmarkAndUrlPattern);
                makeClassFile(companionOnClientClassDom.companionName, ListUtils.join(companionOnClientClassDom.createClass(), "\n"), annotatedElement);
            }
        }
    }


    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    @Override public Set<String> getSupportedAnnotationTypes() {return Set.of(XingYi.class.getName(), XingYiOps.class.getName()); }
}