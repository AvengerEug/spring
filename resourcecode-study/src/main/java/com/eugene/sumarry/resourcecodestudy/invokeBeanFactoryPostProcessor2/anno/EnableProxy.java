package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.anno;

import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.Import.MyImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态为UserServiceImpl添加代理对象
 * 若在AppConfig类中无@EnableProxy注解, 则不代理
 * 否则则代理
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MyImportBeanDefinitionRegistrar.class)
public @interface EnableProxy {
}
