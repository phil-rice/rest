package one.xingyi.restAnnotations.annotations;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.clientside.IXingYiOps;
import one.xingyi.restAnnotations.codedom.*;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.names.OpsNames;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.Strings;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
                log.error(element, "An ops name must end with 'Ops'");
            else {
                findEntity(Optional.of(log), element).ifPresent(entityName -> {
                    OpsNames opsNames = new OpsNames(names, entityNames.entityInterface, new EntityNames(names, entityName));
                    Optional<ElementAndOps> elementAndOps = elementsAndOps.find(entityName);
                    if (elementAndOps.isEmpty()) {
                        log.warning(element, "Cannot find entity " + entityName);
                    }

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
    public static Optional<String> findEntity(Optional<LoggerAdapter> log, TypeElement element) {
        List<TypeMirror> interfaces = (List<TypeMirror>) element.getInterfaces();
        if (interfaces.size() != 1) {
            log.ifPresent(l -> l.error(element, "Expecting one and only one interface, and that to be of type IXingYiOps<T> where T is the entity defining interface"));
            return Optional.empty();
        }
        TypeMirror x = interfaces.get(0);
        String interfaceName = x.toString();
        if (!interfaceName.startsWith(IXingYiOps.class.getName() + "<")) {
            log.ifPresent(l -> l.error(element, "The one and only interface must be of type IXingYiOps<T>"));
            return Optional.empty();
        }
        return Optional.of(Strings.extractFromOptionalEnvelope(IXingYiOps.class.getName(), ">", interfaceName));
    }
}
