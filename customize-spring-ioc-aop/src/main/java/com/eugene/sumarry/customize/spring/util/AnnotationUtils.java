package com.eugene.sumarry.customize.spring.util;

import com.eugene.sumarry.customize.spring.annotation.Description;
import com.eugene.sumarry.customize.spring.annotation.Lazy;
import com.eugene.sumarry.customize.spring.annotation.Primary;
import com.eugene.sumarry.customize.spring.annotation.Scope;
import com.eugene.sumarry.customize.spring.beans.BeanDefinition;
import com.eugene.sumarry.customize.spring.beans.anno.AnnotationBeanDefinition;

import java.util.Arrays;

public class AnnotationUtils {

    public static <T extends BeanDefinition> T fullInBeanDefinition(T beanDefinition, Class<?> clazz) {

        beanDefinition.setBeanClassName(StringUtils.defaultBeanNameCreator(clazz.getSimpleName()));
        beanDefinition.setBeanClass(clazz);

        if (beanDefinition instanceof AnnotationBeanDefinition) {
            ((AnnotationBeanDefinition) beanDefinition).setAnnotations(Arrays.asList(clazz.getAnnotations()));
        }

        if (clazz.isAnnotationPresent(Scope.class)) {
            Scope scope = (Scope) clazz.getAnnotation(Scope.class);
            beanDefinition.setScope(scope.values());
        }

        if (clazz.isAnnotationPresent(Lazy.class)) {
            Lazy lazy = (Lazy) clazz.getAnnotation(Lazy.class);
            beanDefinition.setLazyInit(lazy.values());
        }

        if (clazz.isAnnotationPresent(Primary.class)) {
            Primary primary = (Primary) clazz.getAnnotation(Primary.class);
            beanDefinition.setPrimary(primary.values());
        }

        if (clazz.isAnnotationPresent(Description.class)) {
            Description description = (Description) clazz.getAnnotation(Description.class);
            beanDefinition.setDescription(description.values());
        }

        return beanDefinition;
    }

}
