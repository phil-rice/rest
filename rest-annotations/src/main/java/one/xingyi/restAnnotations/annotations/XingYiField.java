package one.xingyi.restAnnotations.annotations;

import one.xingyi.restAnnotations.utils.Strings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS) // Make it class retention for incremental compilation
public @interface XingYiField {
    String[] readInterfaces() default {};
    String[] writeInterfaces() default {};
    String[] interfaces() default {};
    String lens() default "";
    String javascript() default "";
    boolean deprecated() default false;
}
