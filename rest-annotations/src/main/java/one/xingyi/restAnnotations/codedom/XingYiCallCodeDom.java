package one.xingyi.restAnnotations.codedom;
public class XingYiCallCodeDom {

    public String xingyiGetCall(PackageAndClassName interfaceName, String setOrGet, LensDom lensDom) {
        if (lensDom.toClassName.equals("String"))
            return xingYiStringCall(interfaceName, setOrGet, lensDom);
        else return xingYiObjectCall(interfaceName, setOrGet, lensDom);
    }

    String getLensName(PackageAndClassName interfaceName, LensDom lensDom) {
        return lensDom.fieldDetails.lensName.orElse(interfaceName.className + "_" + lensDom.name) + "_" + lensDom.toClassName;
    }

    private String xingYiObjectCall(PackageAndClassName interfaceName, String setOrGet, LensDom lensDom) {
        return "<" + lensDom.fromClassName + "," + lensDom.toClassName + ">object" + setOrGet + "(\"" + getLensName(interfaceName, lensDom) + "\")";
    }
    private String xingYiStringCall(PackageAndClassName interfaceName, String setOrGet, LensDom lensDom) {
        return "<" + lensDom.fromClassName + ">string" + setOrGet + "(\"" + getLensName(interfaceName, lensDom) + "\")";
    }
}
