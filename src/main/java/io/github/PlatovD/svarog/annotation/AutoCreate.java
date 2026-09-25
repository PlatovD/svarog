package io.github.PlatovD.svarog.annotation;

import io.github.PlatovD.svarog.definition.Scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoCreate {

    Scope scope() default Scope.SINGLETON;

    boolean lazy() default false;

    String name() default "";
}