package Annotation;

import java.lang.annotation.*;

import http.HttpMethod;

@Target({ElementType.METHOD})
@Retention( RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
	String value() default "/";
	HttpMethod method() default HttpMethod.GET;
}
