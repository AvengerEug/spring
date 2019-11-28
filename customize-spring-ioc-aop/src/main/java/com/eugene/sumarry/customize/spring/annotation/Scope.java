package com.eugene.sumarry.customize.spring.annotation;

import com.eugene.sumarry.customize.spring.beans.BeanDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scope {

    String values() default BeanDefinition.SCOPE_SINGLETON;
}
