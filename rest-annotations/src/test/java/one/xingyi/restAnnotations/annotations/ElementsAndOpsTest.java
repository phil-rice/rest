package one.xingyi.restAnnotations.annotations;
import one.xingyi.restAnnotations.codedom.PackageAndClassName;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;
public class ElementsAndOpsTest {
    ////ElementsAndOps(list=[ElementAndOps(main=PackageAndClassName(packageName=one.xingyi.restExample, className=IPerson),
    // interfaces=[IPersonNameOps, IPersonAddressOps, IPersonTelephoneNumberOps],
    // returnedTypes=[java.lang.String, one.xingyi.restExample.IAddress, one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>]),
    // ElementAndOps(main=PackageAndClassName(packageName=one.xingyi.restExample, className=IAddress), interfaces=[IAddressLine12Ops, IAddressLine12Ops], returnedTypes=[java.lang.String]), ElementAndOps(main=PackageAndClassName(packageName=one.xingyi.restExample, className=ITelephoneNumber), interfaces=[], returnedTypes=[java.lang.String])])
    //    final IAddress add
    ElementsAndOps ops = new ElementsAndOps(Arrays.asList(
            new ElementAndOps(new PackageAndClassName("one.xingyi.restExample.IPerson"),
                    Arrays.asList(new InterfaceData("IPersonNameOps", false), new InterfaceData("IPersonAddressOps", true), new InterfaceData("IPersonTelephoneNumberOps", false)),
                    Arrays.asList("java.lang.String, one.xingyi.restExample.IAddress, one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>")),
            new ElementAndOps(new PackageAndClassName("one.xingyi.restExample.IAddress"),
                    Arrays.asList(new InterfaceData("IAddressLine12Ops", false), new InterfaceData("IAddressLine12Ops", false)),
                    Arrays.asList("java.lang.String")),
            new ElementAndOps(new PackageAndClassName("one.xingyi.restExample.ITelephoneNumber"), Arrays.asList(), Arrays.asList("java.lang.String"))));

    @Test
    public void testAllowed() {
        assertEquals(Arrays.asList("java.lang.String", "IPersonNameOps",
                "IPersonAddressOps",
                "IPersonTelephoneNumberOps",//everything after here a bodge until we restrict
                "one.xingyi.restExample.IPerson",
                "one.xingyi.restExample.IAddress",
                "one.xingyi.restExample.ITelephoneNumber"),
                ops.allowedFor("one.xingyi.restExample.IPerson", String.class));
        assertEquals(Arrays.asList("java.lang.String", //everything after here a bodge
                "one.xingyi.restExample.IPerson",
                "one.xingyi.restExample.IAddress",
                "one.xingyi.restExample.ITelephoneNumber"),
                ops.allowedFor(Double.class.getName(), String.class));
    }

//    @Test
//    public void testNeedJavaScriptFor() {
//        assertEquals(Arrays.asList("one.xingyi.restExample.IAddress", "one.xingyi.restExample.ITelephoneNumber"), ops.needJavascriptFor("one.xingyi.restExample.IPerson"));
//        assertEquals(Arrays.asList(), ops.needJavascriptFor("one.xingyi.restExample.IAddress"));
//    }

}