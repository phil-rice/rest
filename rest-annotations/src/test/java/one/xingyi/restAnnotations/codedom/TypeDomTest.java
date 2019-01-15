package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.annotations.ElementsAndOps;
import one.xingyi.restAnnotations.entity.Embedded;
import one.xingyi.restAnnotations.names.INames;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
public class TypeDomTest {

    String className = "a.b.c.E";
    ElementsAndOps elementsAndOps = new ElementsAndOps(Arrays.asList());
    @Test
    public void testTypeDomWithSimpleClass() {
        TypeDom dom = new TypeDom(mock(INames.class), className, elementsAndOps);
        assertEquals(className, dom.fullName);
        assertEquals(className, dom.fullNameOfEntity);
        assertEquals("E", dom.shortName);
        assertEquals(false, dom.embedded);
    }

    //Information:(11, 8) java: Making field details. InterfaceName is [IPerson] name is [telephone[ rawType is[()one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>] typeDom is TypeDom(fullInterfaceName=one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>, fullNameOfEntity=one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>, shortNameOfInterface=ITelephoneNumber>, embedded=false)
    @Test
    public void testTypeDomWithEmbeddedClass() {
        String full = Embedded.class.getName() + "<" + className + ">";
        TypeDom dom = new TypeDom( mock(INames.class), full,elementsAndOps);
        assertEquals(full, dom.fullName);
        assertEquals(className, dom.fullNameOfEntity);
        assertEquals("Embedded<E>", dom.shortName);
        assertEquals(true, dom.embedded);
    }

    @Test
    public void testTypeDomWithRealStrings() {
        String full = "()one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>";
        TypeDom dom = new TypeDom( mock(INames.class), full,elementsAndOps);
        assertEquals("one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>", dom.fullName);
        assertEquals("one.xingyi.restExample.ITelephoneNumber", dom.fullNameOfEntity);
        assertEquals("Embedded<ITelephoneNumber>", dom.shortName);
        assertEquals(true, dom.embedded);
    }

}