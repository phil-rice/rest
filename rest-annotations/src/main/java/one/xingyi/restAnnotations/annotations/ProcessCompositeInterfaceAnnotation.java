package one.xingyi.restAnnotations.annotations;
import one.xingyi.restAnnotations.LoggerAdapter;
import one.xingyi.restAnnotations.clientside.IXingYiMultipleOps;
import one.xingyi.restAnnotations.codedom.CompositeCompanionClassCodeDom;
import one.xingyi.restAnnotations.codedom.CompositeImplClassDom;
import one.xingyi.restAnnotations.names.INames;
import one.xingyi.restAnnotations.names.MultipleInterfaceNames;
import one.xingyi.restAnnotations.utils.ListUtils;
import one.xingyi.restAnnotations.utils.Strings;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Optional;
class ProcessCompositeInterfaceAnnotation extends ProcessAnnotations<XingYiCompositeInterface> {

    private INames names;
    public ProcessCompositeInterfaceAnnotation(INames names,  RoundEnvironment env, Messager messager, Filer filer) {
        super(XingYiCompositeInterface.class, env, messager, filer);
        this.names = names;
    }
    @Override void doit(LoggerAdapter log, TypeElement element, XingYiCompositeInterface annotation) {
        List<String> interfaces = ListUtils.map(element.getInterfaces(), e -> e.toString());
        Optional<String> opt = ListUtils.find(interfaces, s -> s.startsWith(IXingYiMultipleOps.class.getName()));

        if (opt.isEmpty())
            log.error(element, "This interface must extend " + IXingYiMultipleOps.class.getSimpleName());
        opt.ifPresent(multipleInterfaceString -> {

            String nameOfEntity = Strings.extractFromOptionalEnvelope(IXingYiMultipleOps.class.getName(), ">", multipleInterfaceString);

            MultipleInterfaceNames interfaceNames = new MultipleInterfaceNames(names, element.asType().toString(), nameOfEntity);


            CompositeImplClassDom impl = new CompositeImplClassDom(log, interfaceNames);
            makeClassFile(interfaceNames.multipleInterfacesClientImplName, ListUtils.join(impl.createClass(), "\n"), element);

            List<String> cleanedInterfaces = ListUtils.filter(interfaces, i -> !i.startsWith(IXingYiMultipleOps.class.getName()));
            CompositeCompanionClassCodeDom companionDom = new CompositeCompanionClassCodeDom(log, names, interfaceNames, cleanedInterfaces);
            makeClassFile(interfaceNames.multipleInterfacesClientCompanion, ListUtils.join(companionDom.createClass(), "\n"), element);
        });

    }
}
