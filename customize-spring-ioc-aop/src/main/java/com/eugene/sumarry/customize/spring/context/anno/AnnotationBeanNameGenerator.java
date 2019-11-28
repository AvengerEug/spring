package com.eugene.sumarry.customize.spring.context.anno;

import com.eugene.sumarry.customize.spring.beans.BeanDefinition;
import com.eugene.sumarry.customize.spring.beans.CommonBeanDefinition;
import com.eugene.sumarry.customize.spring.context.BeanNameGenerator;

public class AnnotationBeanNameGenerator implements BeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition) {
        if (definition instanceof CommonBeanDefinition) {

        }

        return null;
    }
}
