package com.eugene.sumarry.mybatis.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.stereotype.Component;


/**
 * 该后置处理器的主要作用:
 *   将UserService的beanDefinition标示为byType, 使用此方式后只需要给属性提供set方法即可完成
 *   自动装配(不需要@Autowired注解)
 *
 * 当然, 要是先这样的功能的后置处理器有很多
 * 1. 以@ComponentBeanDefinitionRegistryPostProcessor类型且实现了
 *     Ordered接口的后置处理器
 * 2. 以@ComponentBeanDefinitionRegistryPostProcessor类型没实现
 *     Ordered和PriorityOrdered接口的后置处理器
 * 3. 手动添加的BeanFactoryPostProcessor处理器
 * 4. 以@Component形式添加并实现了PriorityOrdered接口的BeanFactoryPostProcessor
 * 5. 以@Component形式添加并实现了Ordered接口的BeanFactoryPostProcessor后置处理器
 * 6. 以@Component形式添加并未实现PriorityOrdered和Ordered接口的BeanFactoryPostProcessor后置处理器
 * 7. @Import 导入一个ImportBeanDefinitionRegistrar类
 *
 * 总而言之:
 *   就是要在ConfigurationClassPostProcessor执行BeanDefinitionRegistryPostProcessor后置处理器之后执行,
 *   因为执行完它后, spring才会扫描完项目中的所有类, 它对应的beanDefinition才存在, 这样才能修改它的
 *   AutowireMode属性为AbstractBeanDefinition.AUTOWIRE_BY_TYPE
 *
 * 设置beanDefinition的AutowireMode属性为AbstractBeanDefinition.AUTOWIRE_BY_TYPE很重要,
 * 因为在mybatis源码中构建MapperFactoryBean中就用到了它
 */
@Component
public class UserServiceAutowiredByTypeBeanDefinitionRegisterPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ScannedGenericBeanDefinition scannedGenericBeanDefinition = (ScannedGenericBeanDefinition)registry.getBeanDefinition("userService");
        scannedGenericBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
