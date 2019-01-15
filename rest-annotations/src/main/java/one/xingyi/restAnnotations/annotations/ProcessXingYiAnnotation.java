package one.xingyi.restAnnotations.annotations;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.codedom.*;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.utils.ListUtils;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;
class ProcessXingYiAnnotation extends ProcessAnnotations<XingYi> {
    final INames names;
    final ElementsAndOps elementsAndOps;

    public ProcessXingYiAnnotation(INames names, Messager messager, Filer filer, RoundEnvironment env) {
        super(XingYi.class, env, messager, filer);
        this.names = names;
        Set<? extends Element> elements = env.getElementsAnnotatedWith(XingYi.class);
        this.elementsAndOps = ElementsAndOps.create(elements);
    }

    @Override void doit(LoggerAdapter log, TypeElement annotatedElement, XingYi annotation) {
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
