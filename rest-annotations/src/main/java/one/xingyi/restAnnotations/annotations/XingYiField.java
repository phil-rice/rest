package one.xingyi.restAnnotations.annotations;

import one.xingyi.restAnnotations.utils.Strings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS) // Make it class retention for incremental compilation
public @interface XingYiField {
    String lens() default "";
    boolean readOnly() default false;
    String javascript() default "";
    boolean deprecated() default false;
    boolean templatedJson() default false;
}
