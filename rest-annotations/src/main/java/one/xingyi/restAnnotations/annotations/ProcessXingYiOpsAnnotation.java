package one.xingyi.restAnnotations.annotations;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.clientside.IXingYiServerOps;
import one.xingyi.restAnnotations.codedom.*;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.names.OpsNames;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.Strings;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;
public class ProcessXingYiOpsAnnotation extends ProcessAnnotations<XingYiOps> {
    final ElementsAndOps elementsAndOps;
    final INames names;

    public ProcessXingYiOpsAnnotation(INames names, ElementsAndOps elementsAndOps, Messager messager, Filer filer, RoundEnvironment env) {
        super(XingYiOps.class, env, messager, filer);
        this.names = names;
        this.elementsAndOps = elementsAndOps;
    }

    @Override void doit(LoggerAdapter log, TypeElement element, XingYiOps annotation) {
        EntityNames entityNames = new EntityNames(names, element.asType().toString());
//        log.info("Entity names: " + entityNames);
        FieldList fields = FieldList.create(log, names, elementsAndOps, entityNames.entityInterface.className, element.getEnclosedElements());
        List<String> errors = names.validateEntityName(entityNames.entityInterface);
        if (errors.size() > 0) error(element, errors.toString());
        else {
            if (!entityNames.entityInterface.className.endsWith("Ops"))
                log.error(element, "An ops serverName must end with 'Ops'");
            else {
                findEntity(Optional.of(log), element).ifPresent(entityName -> {
                    OpsNames opsNames = new OpsNames(names, entityNames.entityInterface, new EntityNames(names, entityName));
                    Optional<ElementAndOps> elementAndOps = elementsAndOps.find(entityName);
                    if (elementAndOps.isEmpty()) {
                        log.warning(element, "Cannot find entity " + entityName);
                    }

                    OpsClientDom opsClientDom = new OpsClientDom(log, opsNames, fields);
                    makeClassFile(opsClientDom.opsNames.opsClientInterface, ListUtils.join(opsClientDom.createClass(), "\n"), element);

                    OpsServerCompanionClassDom serverCompanionClassDom = new OpsServerCompanionClassDom(opsNames, fields);
                    makeClassFile(serverCompanionClassDom.companionName, ListUtils.join(serverCompanionClassDom.createClass(), "\n"), element);

                    OpsClientCompanionClassDom clientCompanionClassDom = new OpsClientCompanionClassDom(opsNames, fields);
                    makeClassFile(clientCompanionClassDom.companionName, ListUtils.join(clientCompanionClassDom.createClass(), "\n"), element);
//                log.info("finished");
//                makeClassFile(dom.opsName, ListUtils.join(dom.createClass(), "\n"), element);
//            EntityServerDom classDom = new EntityServerDom(log, names, entityNames, fields);
//            for (OpsInterfaceClassDom dom : classDom.nestedOps()) { //needs to be earlier as this makes classes other use
//                makeClassFile(dom.opsName, ListUtils.join(dom.createClass(), "\n"), element);
//            }
//            for (OpsServerCompanionClassDom dom : classDom.nestedOpServerCompanions()) { //needs to be earlier as this makes classes other use
//                makeClassFile(dom.companionName, ListUtils.join(dom.createClass(), "\n"), element);
//            }
//            for (OpsClientCompanionClassDom dom : classDom.nestedOpClientCompanions()) { //needs to be earlier as this makes classes other use
//                makeClassFile(dom.companionName, ListUtils.join(dom.createClass(), "\n"), element);
//            }
                });
            }
        }
    }
    @SuppressWarnings("unchecked")
    public static Optional<String> findEntity(Optional<LoggerAdapter> log, TypeElement element) {
        List<TypeMirror> interfaces = (List<TypeMirror>) element.getInterfaces();
        if (interfaces.size() != 1) {
            log.ifPresent(l -> l.error(element, "Expecting one and only one interface, and that to be of type IXingYiServerOps<T> where T is the entity defining interface"));
            return Optional.empty();
        }
        TypeMirror x = interfaces.get(0);
        String interfaceName = x.toString();
        if (!interfaceName.startsWith(IXingYiServerOps.class.getName() + "<")) {
            log.ifPresent(l -> l.error(element, "The one and only interface must be of type IXingYiServerOps<T>"));
            return Optional.empty();
        }
        return Optional.of(Strings.extractFromOptionalEnvelope(IXingYiServerOps.class.getName(), ">", interfaceName));
    }
}
