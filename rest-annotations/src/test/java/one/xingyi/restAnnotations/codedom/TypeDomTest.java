package one.xingyi.restAnnotations.codedom;
import one.xingyi.restAnnotations.entity.Embedded;
import org.junit.Test;

import static org.junit.Assert.*;
public class TypeDomTest {

    String className = "a.b.c.E";
    @Test
    public void testTypeDomWithSimpleClass() {
        TypeDom dom = new TypeDom(className);
        assertEquals(className, dom.fullName);
        assertEquals(className, dom.fullNameOfEntity);
        assertEquals("E", dom.shortName);
        assertEquals(false, dom.embedded);
    }

    //Information:(11, 8) java: Making field details. InterfaceName is [IPerson] name is [telephone[ rawType is[()one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>] typeDom is TypeDom(fullName=one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>, fullNameOfEntity=one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>, shortName=ITelephoneNumber>, embedded=false)
    @Test
    public void testTypeDomWithEmbeddedClass() {
        String full = Embedded.class.getName() + "<" + className + ">";
        TypeDom dom = new TypeDom(full);
        assertEquals(full, dom.fullName);
        assertEquals(className, dom.fullNameOfEntity);
        assertEquals("Embedded<E>", dom.shortName);
        assertEquals(true, dom.embedded);
    }

@Test
    public void testTypeDomWithRealStrings() {
        //Information:(11, 8) java: Making field details. InterfaceName is [IPerson] name is [telephone[ rawType is[()one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>] typeDom is TypeDom(fullName=one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>, fullNameOfEntity=one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>, shortName=ITelephoneNumber>, embedded=false)

        String full = "()one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>";
        TypeDom dom = new TypeDom(full);
        assertEquals("one.xingyi.restAnnotations.entity.Embedded<one.xingyi.restExample.ITelephoneNumber>", dom.fullName);
        assertEquals("one.xingyi.restExample.ITelephoneNumber", dom.fullNameOfEntity);
        assertEquals("Embedded<ITelephoneNumber>", dom.shortName);
        assertEquals(true, dom.embedded);
    }

}