package one.xingyi.restAnnotations.codedom;
public class XingYiCallCodeDom {

    public String xingyiGetCall(String targetClassname, String setOrGet, LensDom lensDom) {
        if (lensDom.toClassName.equals("String"))
            return xingYiStringCall(setOrGet, lensDom);
        else return xingYiObjectCall(targetClassname, setOrGet, lensDom);
    }

    String domainMakerForObject(String targetClassname, LensDom lensDom) {
        if (lensDom.fieldDetails.type.embedded) {
            return "(e,x) -> Embedded.value(new "+ lensDom.fieldDetails.type.clientImplName + "(e, x))";
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
