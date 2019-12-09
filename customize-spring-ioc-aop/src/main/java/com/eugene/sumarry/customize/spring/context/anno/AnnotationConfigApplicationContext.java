package com.eugene.sumarry.customize.spring.context.anno;

import com.eugene.sumarry.customize.spring.annotation.ComponentScan;
import com.eugene.sumarry.customize.spring.annotation.Config;
import com.eugene.sumarry.customize.spring.beans.AnnotatedGenericBeanDefinition;
import com.eugene.sumarry.customize.spring.beans.BeanFactory;
import com.eugene.sumarry.customize.spring.beans.DefaultListableBeanFactory;
import com.eugene.sumarry.customize.spring.beans.RootBeanDefinition;
import com.eugene.sumarry.customize.spring.util.AnnotationConfigUtils;
import com.eugene.sumarry.customize.spring.util.Assert;
import com.eugene.sumarry.customize.spring.util.PostProcessorRegistrationDelegate;

public class AnnotationConfigApplicationContext extends DefaultAnnotationConfigApplicationContext {

    private AnnotatedBeanDefinitionReader reader;

    private ClassPathBeanDefinitionScanned scanned;

    public AnnotationConfigApplicationContext() {
        reader = new AnnotatedBeanDefinitionReader(this);
        scanned = new ClassPathBeanDefinitionScanned(this);
    }

    public DefaultListableBeanFactory getBeanFactory() {
        return super.beanFactory;
    }

    /**
     * spring源码中register的功能
     * @param configClz
     */
    public void register(Class<?> configClz) {
        Assert.isTrue(configClz.isAnnotationPresent(Config.class), () -> {
            return "传入类非java config类";
        });

        if (!Assert.isTrue(configClz.isAnnotationPresent(ComponentScan.class), true)) return;

        registerConfig(configClz);
    }

    private void registerConfig(Class<?> annotatedConfig) {
        AnnotatedGenericBeanDefinition beanDefinition = AnnotationConfigUtils.constructConfigBean(annotatedConfig);
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
        invokeBeanFactoryPostProcessors(this.getBeanFactory());
    }

    private void invokeBeanFactoryPostProcessors(DefaultListableBeanFactory beanFactory) {
        PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());

        /*ComponentScan componentScan = configClz.getAnnotation(ComponentScan.class);
        String pkg = getPkgPath(componentScan);
        reader.doScan(pkg);
        reader.fullInBeanDefinition(null);*/
    }


}
