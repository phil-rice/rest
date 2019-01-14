package one.xingyi.restAnnotations.codedom;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.xingyi.restAnnotations.utils.OptionalUtils;

import java.util.Optional;
@ToString
@EqualsAndHashCode

public class BookmarkAndUrlPattern {
    final public String bookmark;
    final public String urlPattern;
    public BookmarkAndUrlPattern(String entityName, String bookmarked, String urlPattern) {
        this.bookmark = OptionalUtils.fromString(bookmarked).orElse("/" + entityName).toLowerCase();
        this.urlPattern = OptionalUtils.fromString(bookmarked).orElse("/" + entityName + "/<id>").toLowerCase();
    }
}
