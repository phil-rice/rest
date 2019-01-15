package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.utils.OptionalUtils;
public class XingYiCallCodeDom {

    public String xingyiGetCall(String setOrGet, LensDom lensDom) {
        return OptionalUtils.fold(lensDom.fieldDetails.type.optEntityName,
                () -> {
                    if (!lensDom.toClassName.equals("String"))
                        throw new RuntimeException("mistakenly tried to set a string call" + lensDom);
                    return xingYiStringCall(setOrGet, lensDom);
                }, en -> xingYiObjectCall(en.clientImplementation.className, setOrGet, lensDom));

    }

    String domainMakerForObject(String targetClassname, LensDom lensDom) {
        if (lensDom.fieldDetails.type.embedded) {
            return "(e,x) -> Embedded.value(new " + targetClassname + "(e, x))";
        } else
            return targetClassname + "::new";
    }

    String xingYiObjectCall(String targetClassname, String setOrGet, LensDom lensDom) {
        return "<" + lensDom.fromClassName + "," + lensDom.toClassName + ">object" + setOrGet + "(" + lensDom.fromClassName + "::new," + domainMakerForObject(targetClassname, lensDom) + ", \"lens_" + lensDom.fieldDetails.lensName + "\")";
    }
    String xingYiStringCall(String setOrGet, LensDom lensDom) {
        return "<" + lensDom.fromClassName + ">string" + setOrGet + "(" + lensDom.fromClassName + "::new, \"lens_" + lensDom.fieldDetails.lensName + "\")";
    }
}
