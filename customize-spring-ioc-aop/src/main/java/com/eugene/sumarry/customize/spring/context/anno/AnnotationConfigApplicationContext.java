package com.eugene.sumarry.customize.spring.context.anno;

import com.eugene.sumarry.customize.spring.annotation.ComponentScan;
import com.eugene.sumarry.customize.spring.annotation.Config;
import com.eugene.sumarry.customize.spring.beans.BeanFactory;
import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;
import com.eugene.sumarry.customize.spring.beans.RootBeanDefinition;
import com.eugene.sumarry.customize.spring.util.AnnotationConfigUtils;
import com.eugene.sumarry.customize.spring.util.Assert;

public class AnnotationConfigApplicationContext extends DefaultAnnotationConfigApplicationContext {

    private AnnotatedBeanDefinitionReader reader;

    public AnnotationConfigApplicationContext() {
        reader = new AnnotatedBeanDefinitionReader(this);
    }

    public DefaultListableBeanFactory getBeanFactory() {
        return super.beanFactory;
    }

    /**
     * spring源码中register的功能
     * @param configClz
     */
    public void register(Class<?> configClz) {
        initResourcePath(configClz);
        registerConfig(configClz);
    }

    private void registerConfig(Class<?> configClz) {
        RootBeanDefinition beanDefinition = AnnotationConfigUtils.constructConfigBean(configClz);
        this.getBeanFactory().addBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
        this.getBeanFactory().addBeanDefinitionName(beanDefinition.getBeanClassName());
    }

    public AnnotationConfigApplicationContext(Class<?> configClz) {
        this();
        this.register(configClz);
        this.refresh();
    }

    @Override
    public void refresh() {
        Assert.isTrue(configClz.isAnnotationPresent(Config.class), () -> {
            return "传入类非java config类";
        });

        if (!Assert.isTrue(configClz.isAnnotationPresent(ComponentScan.class), true)) return;

        ComponentScan componentScan = configClz.getAnnotation(ComponentScan.class);
        String pkg = getPkgPath(componentScan);
        reader.doScan(pkg);
        reader.fullInBeanDefinition(null);
    }

    private String getPkgPath(ComponentScan componentScan) {
        return resourcePath + componentScan.value().replace(SPORT, FILE_SEPARATOR);
    }


}
