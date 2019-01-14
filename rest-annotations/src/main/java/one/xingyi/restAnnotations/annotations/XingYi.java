package one.xingyi.restAnnotations.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS) // Make it class retention for incremental compilation
public @interface XingYi {
    /**
     * where the client can get an entity from: must have <id> and be of shape xxx/<id>/yyy (xxx and yy can be empty
     * This can change between deployments: it is discovered by the clients
     */
    String urlPattern() default "";

    /**
     * where the client can get an entity from. This is a 'just this value' url such as /person
     * This should be a very stable value as the clients use this for url discovery
     */
    String bookmarked() default "";
}
