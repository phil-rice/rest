package one.xingyi.restAnnotations.clientside;
import java.net.http.HttpClient;
public class Client {
    HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)  // this is the default
            .build();
}
