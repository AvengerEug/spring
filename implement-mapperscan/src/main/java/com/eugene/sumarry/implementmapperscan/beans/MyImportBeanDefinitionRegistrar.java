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

            // 这行代码是为了在spring实例化bean的时候 调用它的一个带参的构造方法, 并将参数值传进去
            // 然后当前实例化的bean是一个FactoryBean, 所以我们可以在它的getObject方法中返回我们传入参数的代理对象出去
            // mybatis就是这样实现的: 调用地址org.mybatis.spring.mapper.ClassPathMapperScanner.processBeanDefinitions
            builder.getBeanDefinition().getConstructorArgumentValues().addGenericArgumentValue(UserDao.class.getName());
            registry.registerBeanDefinition("userDao", builder.getBeanDefinition());
        }
    }
}
