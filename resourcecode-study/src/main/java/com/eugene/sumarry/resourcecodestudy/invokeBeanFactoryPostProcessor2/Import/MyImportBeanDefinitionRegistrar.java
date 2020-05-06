package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.Import;

import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.anno.EnableProxy;
import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.postprocessor.JDKProxyPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 模拟spring aop, 这里添加了一个创建jdk动态代理的后置处理器
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        if (importingClassMetadata.hasAnnotation(EnableProxy.class.getName())) {
            GenericBeanDefinition genericBeanDefinition = (GenericBeanDefinition)
                    registry.getBeanDefinition("userServiceImpl");
            // 模拟mybatis的SqlSessionFactoryBean, 为MapperScan对象设置自动装配模式
            // ps: UserServiceImpl类中的UserDaoImpl属性没有添加@Autowired注解只有set方法, 
            // 若不添加此行代码, 属性注入不进
            genericBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

            RootBeanDefinition beanDefinition = new RootBeanDefinition(
                    JDKProxyPostProcessor.class);
            registry.registerBeanDefinition(JDKProxyPostProcessor.class.getSimpleName(), 
                    beanDefinition);
        }
    }
}
