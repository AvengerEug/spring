package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.anno;

import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.Import.ImportEugeneImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态添加ImportEugeneBeanFactoryProcessor
 * 若在AppConfig类中添加@ImportEugene注解,
 * 则会添加ImportEugeneBeanFactoryProcessor后置处理器
 * 否则不添加
 *
 * ImportEugeneBeanFactoryProcessor后置处理器的作用就是
 * 在创建bean之前在控制台输出 "========ImportEugene========"
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ImportEugeneImportSelector.class)
public @interface ImportEugene {
}
