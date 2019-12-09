package com.eugene.sumarry.implementmapperscan.beans;

import com.eugene.sumarry.implementmapperscan.dao.UserDao;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 配置@Import注解使用
 * 参考AppConfig类的@Import注解
 * 使用方式: 在AppConfig类中添加如下代码
 * @Import( { MyImportBeanDefinitionRegistrar.class })
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 该后置处理器会被调用多次
        if (!registry.containsBeanDefinition("userDao")) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(UserDaoFactoryBean.class);
            builder.getBeanDefinition().getConstructorArgumentValues().addGenericArgumentValue(UserDao.class.getName());

            registry.registerBeanDefinition("userDao", builder.getBeanDefinition());
        }
    }
}
