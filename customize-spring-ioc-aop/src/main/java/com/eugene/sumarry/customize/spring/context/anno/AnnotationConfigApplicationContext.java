package com.eugene.sumarry.customize.spring.context.anno;

import com.eugene.sumarry.customize.spring.annotation.ComponentScan;
import com.eugene.sumarry.customize.spring.annotation.Config;
import com.eugene.sumarry.customize.spring.util.Assert;

public class AnnotationConfigApplicationContext extends DefaultAnnotationConfigApplicationContext {

    private AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader();

    public AnnotationConfigApplicationContext() {
    }

    /**
     * spring源码中register的功能
     * @param configClz
     */
    public void register(Class<?> configClz) {
        initResourcePath(configClz);
    }

    public AnnotationConfigApplicationContext(Class<?> configClz) {
        super(configClz);
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
        System.out.println(reader);
    }

    private String getPkgPath(ComponentScan componentScan) {
        return resourcePath + componentScan.value().replace(SPORT, FILE_SEPARATOR);
    }


}
