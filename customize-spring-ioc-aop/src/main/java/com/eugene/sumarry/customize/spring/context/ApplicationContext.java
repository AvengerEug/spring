package com.eugene.sumarry.customize.spring.context;

import com.eugene.sumarry.customize.spring.annotation.ComponentScan;
import com.eugene.sumarry.customize.spring.annotation.Config;
import com.eugene.sumarry.customize.spring.util.Assert;

import java.lang.annotation.Annotation;

public class ApplicationContext {

    private Class<?> configClz;

    private String resourcePath;

    public ApplicationContext(Class<?> configClz) {
        this.configClz = configClz;
        this.initResourcePath(configClz);
        this.registerApplication();
    }

    private void initResourcePath(Class<?> configClz) {
        this.resourcePath = configClz.getResource("/").toString();
    }

    private void registerApplication() {
        Assert.isTrue(configClz.isAnnotationPresent(Config.class), () -> {
            return "传入类非java config类";
        });

        if (!Assert.isTrue(configClz.isAnnotationPresent(ComponentScan.class), true)) return;

        ComponentScan componentScan = configClz.getAnnotation(ComponentScan.class);
        String pkg = getPkgPath(componentScan);
        System.out.println(pkg);

    }

    private String getPkgPath(ComponentScan componentScan) {
        return resourcePath + componentScan.value().replace(".", "/");
    }
}
