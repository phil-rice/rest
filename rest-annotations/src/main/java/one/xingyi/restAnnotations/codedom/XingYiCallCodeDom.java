package one.xingyi.restAnnotations.codedom;
public class XingYiCallCodeDom {

    public String xingyiGetCall(PackageAndClassName interfaceName, String targetClassname, String setOrGet, LensDom lensDom) {
        if (lensDom.toClassName.equals("String"))
            return xingYiStringCall(interfaceName, targetClassname, setOrGet, lensDom);
        else return xingYiObjectCall(interfaceName, targetClassname, setOrGet, lensDom);
    }


    private String xingYiObjectCall(PackageAndClassName interfaceName, String targetClassname, String setOrGet, LensDom lensDom) {
        return "<" + lensDom.fromClassName + "," + lensDom.toClassName + ">object" + setOrGet + "(" + lensDom.fromClassName + "::new," + targetClassname + "::new, \"lens_" + lensDom.fieldDetails.lensName + "\")";
    }
    private String xingYiStringCall(PackageAndClassName interfaceName, String targetClassname, String setOrGet, LensDom lensDom) {
        return "<" + lensDom.fromClassName + ">string" + setOrGet + "(" + lensDom.fromClassName + "::new, \"lens_" + lensDom.fieldDetails.lensName + "\")";
    }
}
