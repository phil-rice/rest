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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
class ProcessXingYiServerAnnotation extends ProcessAnnotations<XingYiServer> {
    final INames names;
    final ElementsAndOps elementsAndOps;

    public ProcessXingYiServerAnnotation(INames names, ElementsAndOps elementsAndOps, Messager messager, Filer filer, RoundEnvironment env) {
        super(XingYiServer.class, env, messager, filer);
        this.names = names;
        this.elementsAndOps = elementsAndOps;
    }

    @Override boolean validateElements(LoggerAdapter log, Set<? extends Element> elements) {
        if (elements.size() > 1) {
            elements.forEach(e -> log.error(e, "Can only have one server annotation"));
        }
        return super.validateElements(log, elements);
    }
    @Override void doit(LoggerAdapter log, TypeElement element, XingYiServer annotation) {
        List<Element> entities = Arrays.asList(env.getElementsAnnotatedWith(XingYi.class).toArray(new Element[0]));
        List<EntityDetails> entityNames = ListUtils.map(entities, e -> makeEntityDetails(e));
        if (entities.size() == 0)
            log.warning(element, "There are no annotated entities. This might be an issue with incrementall compilation. A full compile is recommended");
        EntityNames serverEntityName = new EntityNames(names, element.asType().toString());
        ServerDom serverDom = new ServerDom(serverEntityName, entityNames);
        makeClassFile(serverEntityName.serverImplementation, ListUtils.join(serverDom.createClass(), "\n"), element);

    }
    private EntityDetails makeEntityDetails(Element e) {
        EntityNames entityNames = new EntityNames(names, e.asType().toString());
        boolean hasUrlPattern = e.getAnnotation(XingYi.class).urlPattern().length() > 0;
        return new EntityDetails(entityNames, hasUrlPattern);
    }
}
