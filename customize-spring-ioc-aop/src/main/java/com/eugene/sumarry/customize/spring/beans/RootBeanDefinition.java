package com.eugene.sumarry.customize.spring.beans;

import com.eugene.sumarry.customize.spring.util.StringUtils;

public class RootBeanDefinition extends CommonBeanDefinition {

    public RootBeanDefinition(Class<?> clazz) {
        this.setBeanClass(clazz);
        this.setBeanClassName(StringUtils.defaultBeanNameCreator(clazz.getSimpleName()));
        this.setScope(BeanDefinition.SCOPE_SINGLETON);
        this.setDescription("This is RootBeanDefinition");
        this.setLazyInit(false);
        this.setPrimary(true);
    }

}
