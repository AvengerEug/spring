package com.eugene.sumarry.customize.spring.context.anno;

import com.eugene.sumarry.customize.spring.beans.BeanDefinitionRegistry;
import com.eugene.sumarry.customize.spring.util.AnnotationConfigUtils;

public class AnnotatedBeanDefinitionReader {

    private BeanDefinitionRegistry registry;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
        AnnotationConfigUtils.registerAnnotationConfigProcessors(registry);
    }

}
