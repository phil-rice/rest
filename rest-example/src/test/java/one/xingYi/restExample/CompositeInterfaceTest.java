package one.xingYi.restExample;
import one.xingyi.restAnnotations.client.Client;
import one.xingyi.restAnnotations.clientside.JavaHttpClient;
import one.xingyi.restAnnotations.entity.EntityRegister;
import one.xingyi.restAnnotations.javascript.IXingYi;
import one.xingyi.restExample.DomainClient;
import one.xingyi.restExample.PersonServer;
import one.xingyi.restcore.entity.SimpleEntityRegister;
import one.xingyi.restcore.xingyiclient.SimpleClient;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.mock;
public class CompositeInterfaceTest {

    @Test public void testCanFindCompanionOnClient() {
        assertEquals(TestMultipleClientCompanion.companion, TestMultipleClientCompanion.companion.findCompanion().apply(ITestMultiple.class).get());
    }
    @Test public void testCanCreateCompanio() {
        IXingYi xingYi = mock(IXingYi.class);
        TestMultipleClientImpl testMultiple = (TestMultipleClientImpl) TestMultipleClientCompanion.companion.apply(ITestMultiple.class, xingYi, "mirror").get();
        assertEquals("mirror",testMultiple.mirror) ;
        assertEquals(xingYi, testMultiple.xingYi) ;

    }

    @Test public void testCanFindWhenAddedToEntityRegisterOnClient() {
        SimpleClient client = (SimpleClient) DomainClient.client("somePrefix", JavaHttpClient.client, TestMultipleClientCompanion.companion);
        assertEquals(TestMultipleClientCompanion.companion, client.factory.findCompanion().apply(ITestMultiple.class).get());
    }


}
