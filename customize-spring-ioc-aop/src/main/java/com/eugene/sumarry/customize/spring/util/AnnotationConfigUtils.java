package com.eugene.sumarry.customize.spring.util;

import com.eugene.sumarry.customize.spring.beans.BeanDefinition;
import com.eugene.sumarry.customize.spring.beans.BeanDefinitionRegistry;
import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;
import com.eugene.sumarry.customize.spring.beans.RootBeanDefinition;
import com.eugene.sumarry.customize.spring.context.Context;
import com.eugene.sumarry.customize.spring.context.anno.AnnotationConfigApplicationContext;

public class AnnotationConfigUtils {

    // 目前还不清楚spring内置bean是否是动态产生的, 所以先随便定义个RootBeanDefinition

    public static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME =
            "com.eugene.sumarry.customize.spring.context.internalConfigurationAnnotationProcessor";


    public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry beanDefinitionRegistry) {
        DefaultListableBeanFactory beanFactory = unwrapDefaultListableBeanFactory(beanDefinitionRegistry);

        if (!beanFactory.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            RootBeanDefinition def = new RootBeanDefinition();
            beanFactory.addBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME, def);
            beanFactory.addBeanDefinitionName(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME);
        }
    }

    private static DefaultListableBeanFactory unwrapDefaultListableBeanFactory(BeanDefinitionRegistry registry) {
        Context context = (Context) registry;
        if (context instanceof AnnotationConfigApplicationContext) {
            return ((AnnotationConfigApplicationContext)context).getBeanFactory();
        }

        return null;
    }

    public static RootBeanDefinition constructConfigBean(Class<?> clazz) {
        return AnnotationUtils.fullInBeanDefinition(new RootBeanDefinition(), clazz);
    }
}
