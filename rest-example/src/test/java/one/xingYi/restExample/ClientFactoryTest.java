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
        assertEquals(Set.of(ITest11Ops.class, ITest12Ops.class), companion1.supported());
        assertEquals(Set.of(ITest22Ops.class), companion2.supported());
        assertEquals(Set.of(ITest11Ops.class, ITest12Ops.class, ITest22Ops.class), composed.supported());
    }

    @Test
    public void testFindCompanion() {
        assertEquals(companion1, companion1.findCompanion().apply(ITest11Ops.class).get());
        assertEquals(companion1, companion1.findCompanion().apply(ITest12Ops.class).get());
        assertEquals(companion2, companion2.findCompanion().apply(ITest22Ops.class).get());

        assertEquals(companion1, composed.findCompanion().apply(ITest11Ops.class).get());
        assertEquals(companion1, composed.findCompanion().apply(ITest12Ops.class).get());
        assertEquals(companion2, composed.findCompanion().apply(ITest22Ops.class).get());

        assertEquals(Optional.empty(), companion1.findCompanion().apply(ITest22Ops.class));
        assertEquals(Optional.empty(), companion2.findCompanion().apply(ITest12Ops.class));
        assertEquals(Optional.empty(), companion2.findCompanion().apply(String.class));

    }
}
