package one.xingyi.restAnnotations.endpoints;

import one.xingyi.restAnnotations.http.ServiceRequest;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
public class EndpointAcceptor1Test {
    ServiceRequest srGetJustOne = new ServiceRequest("getEntity", "/one", Arrays.asList(), "");
    ServiceRequest srGetJustOneAndExtraSlash = new ServiceRequest("getEntity", "/one/", Arrays.asList(), "");
    ServiceRequest srGetTwo = new ServiceRequest("getEntity", "/one/two", Arrays.asList(), "");
    ServiceRequest srPutJustOne = new ServiceRequest("put", "/one", Arrays.asList(), "");

    EndpointAcceptor1<String> acceptor1 = EndpointAcceptor1.justOneThing("getEntity", sr -> sr);
    @Test
    public void testJustOneThing() {
        System.out.println(Arrays.asList(srGetTwo.urlSegments()));
        assertEquals(Optional.of("one"), acceptor1.apply(srGetJustOne));
        assertEquals(Optional.of("one"), acceptor1.apply(srGetJustOneAndExtraSlash));
        assertEquals(Optional.empty(), acceptor1.apply(srGetTwo));
        assertEquals(Optional.empty(), acceptor1.apply(srPutJustOne));
    }
}
