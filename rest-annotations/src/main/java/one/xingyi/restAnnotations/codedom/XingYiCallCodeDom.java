package one.xingyi.restAnnotations.codedom;
public class XingYiCallCodeDom {

    public String xingyiGetCall(PackageAndClassName interfaceName, String setOrGet, LensDom lensDom) {
        if (lensDom.toClassName.equals("String"))
            return xingYiStringCall(interfaceName, setOrGet, lensDom);
        else return xingYiObjectCall(interfaceName, setOrGet, lensDom);
    }


    private String xingYiObjectCall(PackageAndClassName interfaceName, String setOrGet, LensDom lensDom) {
        return "<" + lensDom.fromClassName + "," + lensDom.toClassName + ">object" + setOrGet + "(\"" + lensDom.fieldDetails.lensName + "\")";
    }
    private String xingYiStringCall(PackageAndClassName interfaceName, String setOrGet, LensDom lensDom) {
        return "<" + lensDom.fromClassName + ">string" + setOrGet + "(\"" + lensDom.fieldDetails.lensName + "\")";
    }
}
