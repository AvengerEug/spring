package com.eugene.sumarry.customize.spring.context.anno;


/**
 * 默认注解上下文类
 *
 * 初始化配置类属性、构建扫描路径
 *
 */
public abstract class DefaultAnnotationConfigApplicationContext implements AnnotationContext {

    protected Class<?> configClz;

    protected String resourcePath;

    public DefaultAnnotationConfigApplicationContext() {
        System.out.println("parent");
    }

    public DefaultAnnotationConfigApplicationContext(Class<?> configClz) {
        this.initResourcePath(configClz);
    }

    protected void initResourcePath(Class<?> configClz) {
        this.configClz = configClz;
        this.resourcePath = configClz.getResource("/").toString();
    }

    protected abstract void refresh();
}
