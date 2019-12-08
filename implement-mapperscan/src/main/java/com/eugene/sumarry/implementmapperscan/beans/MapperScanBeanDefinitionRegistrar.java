package com.eugene.sumarry.implementmapperscan.beans;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class MapperScanBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {


        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();

        registry.registerBeanDefinition("userDao", builder.getBeanDefinition());

    }
}
