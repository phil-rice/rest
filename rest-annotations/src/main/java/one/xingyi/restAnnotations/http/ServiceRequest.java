package one.xingyi.restAnnotations.http;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ServiceRequest {
    final public String method;
    final public String url;
    final public List<Header> headers;
    final public String body;

    private String[] urlSegments = null;

    public int segmentsCount() {return urlSegments().length;}
    public String[] urlSegments() {
        if (urlSegments == null)
            urlSegments = url.split("/");
        return urlSegments;
    }
    public String lastSegment() { return urlSegments()[urlSegments().length - 1]; }


}