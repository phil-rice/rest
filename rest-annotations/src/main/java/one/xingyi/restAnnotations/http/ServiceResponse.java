package one.xingyi.restAnnotations.http;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ServiceResponse {
    public final int statusCode;
    public final String body;
    public final List<Header> headers;


    public static ServiceResponse html(int status, String body) {
        return new ServiceResponse(status, body, Arrays.asList(new Header("Content-type", "text/html")));

    }
}