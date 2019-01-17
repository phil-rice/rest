package one.xingyi.restAnnotations.annotations;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.codedom.*;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.utils.ListUtils;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;
class ProcessXingYiAnnotation extends ProcessAnnotations<XingYi> {
    final INames names;
    final ElementsAndOps elementsAndOps;

    public ProcessXingYiAnnotation(INames names, ElementsAndOps elementsAndOps, Messager messager, Filer filer, RoundEnvironment env) {
        super(XingYi.class, env, messager, filer);
        this.names = names;
        this.elementsAndOps = elementsAndOps;
    }

    @Override void doit(LoggerAdapter log, TypeElement element, XingYi annotation) {
        EntityNames entityNames = new EntityNames(names, element.asType().toString());
        FieldList fields = FieldList.create(log, names, elementsAndOps, entityNames.entityInterface.className, element.getEnclosedElements());
        if (fields.isEmpty()){
            log.error(element,"There is no data in the object. You must have at least one field. Like 'String serverName()'");
        }
        List<String> errors = names.validateEntityName(entityNames.entityInterface);
        if (errors.size() > 0) error(element, errors.toString());
        else {
            BookmarkAndUrlPattern bookmarkAndUrlPattern = new BookmarkAndUrlPattern(entityNames.serverImplementation.className, annotation.bookmarked(), annotation.urlPattern());

            EntityServerDom serverDom = new EntityServerDom(log, names, entityNames, fields);
            makeClassFile(serverDom.packageAndClassName, ListUtils.join(serverDom.createClass(), "\n"), element);

            List<InterfaceData> interfaceNames = elementsAndOps.findInterfaces(entityNames.entityInterface.asString());
            EntityClientDom clientDom = new EntityClientDom(log, names, entityNames, fields, interfaceNames);
            makeClassFile(clientDom.clientImpl, ListUtils.join(clientDom.createClass(), "\n"), element);


            EntityServerCompanionDom entityServerCompanionDom = new EntityServerCompanionDom(log, names, elementsAndOps, entityNames, fields, bookmarkAndUrlPattern);
            makeClassFile(entityServerCompanionDom.companionName, ListUtils.join(entityServerCompanionDom.createClass(), "\n"), element);

            EntityClientCompanionDom entityClientCompanionDom = new EntityClientCompanionDom(log, names, elementsAndOps, entityNames, fields, bookmarkAndUrlPattern);
            makeClassFile(entityClientCompanionDom.companionName, ListUtils.join(entityClientCompanionDom.createClass(), "\n"), element);


        }
    }
}
