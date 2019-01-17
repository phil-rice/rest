package one.xingyi.restAnnotations.annotations;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.codedom.ClientDom;
import one.xingyi.restAnnotations.names.EntityNames;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.utils.ListUtils;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
class ProcessXingYiClientAnnotation extends ProcessAnnotations<XingYiClient> {
    final INames names;

    public ProcessXingYiClientAnnotation(INames names, Messager messager, Filer filer, RoundEnvironment env) {
        super(XingYiClient.class, env, messager, filer);
        this.names = names;
    }

    @Override boolean validateElements(LoggerAdapter log, Set<? extends Element> elements) {
        if (elements.size() > 1) {
            elements.forEach(e -> log.error(e, "Can only have one client annotation" + elements));
        }
        return super.validateElements(log, elements);
    }
    @Override void doit(LoggerAdapter log, TypeElement element, XingYiClient annotation) {
        List<Element> entities = Arrays.asList(env.getElementsAnnotatedWith(XingYi.class).toArray(new Element[0]));
        List<EntityNames> entityNames = ListUtils.map(entities, e -> new EntityNames(names, e.asType().toString()));
        if (entities.size() == 0)
            log.warning(element, "There are no annotated entities. This might be an issue with incrementall compilation. A full compile is recommended");
        EntityNames serverEntityName = new EntityNames(names, element.asType().toString());
        ClientDom clientDom = new ClientDom(serverEntityName, entityNames);
        makeClassFile(serverEntityName.serverImplementation, ListUtils.join(clientDom.createClass(), "\n"), element);

    }
}
