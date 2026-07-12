package mg.eddy.annotation;

import java.lang.annotation.*;
import mg.eddy.mapping.HTTPMethod;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface APIAnnotation {
    String value() default "";
    boolean enabled() default true;
    HTTPMethod httpMethod() default HTTPMethod.GET;
}
