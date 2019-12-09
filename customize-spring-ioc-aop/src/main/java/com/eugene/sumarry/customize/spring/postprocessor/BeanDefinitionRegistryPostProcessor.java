package com.eugene.sumarry.customize.spring.postprocessor;

import com.eugene.sumarry.customize.spring.beans.BeanDefinitionRegistry;

public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {

    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry);
}
