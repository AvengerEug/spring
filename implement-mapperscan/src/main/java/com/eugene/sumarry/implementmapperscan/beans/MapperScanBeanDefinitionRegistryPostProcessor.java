package com.eugene.sumarry.implementmapperscan.beans;

import com.eugene.sumarry.implementmapperscan.dao.UserDao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * 可以使用上下文api手动添加,
 * eg: context.addBeanFactoryPostProcessor(new MapperScanBeanDefinitionRegistryPostProcessor());
 * 手动添加则不需要添加@Component注解
 *
 * => 手动添加BeanFactoryPostProcessor和使用@Component注解方式的不同:
 *   1. 手动添加的方式是最先被执行的后置处理器, 比spring内置的后置处理器还早
 */
public class MapperScanBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        // 该后置处理器会被调用多次
        if (!beanDefinitionRegistry.containsBeanDefinition("userDao")) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(UserDaoFactoryBean.class);
            builder.getBeanDefinition().getConstructorArgumentValues().addGenericArgumentValue(UserDao.class.getName());

            beanDefinitionRegistry.registerBeanDefinition("userDao", builder.getBeanDefinition());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }
}
