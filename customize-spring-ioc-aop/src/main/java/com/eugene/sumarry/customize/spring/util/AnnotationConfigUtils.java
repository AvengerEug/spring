package com.eugene.sumarry.customize.spring.util;

import com.eugene.sumarry.customize.spring.beans.*;
import com.eugene.sumarry.customize.spring.context.Context;
import com.eugene.sumarry.customize.spring.context.anno.AnnotationConfigApplicationContext;
import com.eugene.sumarry.customize.spring.postprocessor.ConfigurationClassPostProcessor;

public class AnnotationConfigUtils {

    // 目前还不清楚spring内置bean是否是动态产生的, 所以先随便定义个RootBeanDefinition

    public static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME =
            "com.eugene.sumarry.customize.spring.context.internalConfigurationAnnotationProcessor";


    public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry beanDefinitionRegistry) {
        DefaultListableBeanFactory beanFactory = unwrapDefaultListableBeanFactory(beanDefinitionRegistry);

        if (!beanFactory.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);
            beanFactory.addBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME, def);
            beanFactory.addBeanDefinitionName(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME);
        }
    }

    private static DefaultListableBeanFactory unwrapDefaultListableBeanFactory(BeanDefinitionRegistry registry) {
        if (registry instanceof AnnotationConfigApplicationContext) {
            return ((AnnotationConfigApplicationContext)registry).getBeanFactory();
        } else if (registry instanceof DefaultListableBeanFactory) {
            return ((DefaultListableBeanFactory) registry);
        }

        return null;
    }

    public static AnnotatedGenericBeanDefinition constructConfigBean(Class<?> clazz) {
        return AnnotationUtils.fullInBeanDefinition(new AnnotatedGenericBeanDefinition(), clazz);
    }
}
