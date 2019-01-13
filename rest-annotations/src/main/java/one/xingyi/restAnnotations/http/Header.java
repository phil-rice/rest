package one.xingyi.restAnnotations.http;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Header {
    public final String name;
    public final String value;
}
