package one.xingYi.restExample;
import one.xingyi.restAnnotations.clientside.IClientFactory;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
public class ClientFactoryTest {

    ClientFactoryTest1ClientCompanion companion1 = ClientFactoryTest1ClientCompanion.companion;
    ClientFactoryTest2ClientCompanion companion2 = ClientFactoryTest2ClientCompanion.companion;
    IClientFactory composed = IClientFactory.compose(companion1, companion2);

    @Test
    public void testCompanionHasSupported() {
        assertEquals(Set.of(ITest11.class, ITest12.class), companion1.supported());
        assertEquals(Set.of(ITest22.class), companion2.supported());
        assertEquals(Set.of(ITest11.class, ITest12.class, ITest22.class), composed.supported());
    }

    @Test
    public void testFindCompanion() {
        assertEquals(Test11ClientCompanion.companion, companion1.findCompanion().apply(ITest11.class).get());
        assertEquals(Test12ClientCompanion.companion, companion1.findCompanion().apply(ITest12.class).get());
        assertEquals(Test22ClientCompanion.companion, companion2.findCompanion().apply(ITest22.class).get());

        assertEquals(Test11ClientCompanion.companion, composed.findCompanion().apply(ITest11.class).get());
        assertEquals(Test12ClientCompanion.companion, composed.findCompanion().apply(ITest12.class).get());
        assertEquals(Test22ClientCompanion.companion, composed.findCompanion().apply(ITest22.class).get());

        assertEquals(Optional.empty(), companion1.findCompanion().apply(ITest22.class));
        assertEquals(Optional.empty(), companion2.findCompanion().apply(ITest12.class));
//        assertEquals(Optional.empty(), companion2.findCompanion().apply(String.class));

    }
}
