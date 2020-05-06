package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor4;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

/**
 * 此后置处理器的postProcessAfterInstantiation
 * 方法返回false，那么所有bean的@Autowired注解功能都失效
 *
 * 具体原因:
 * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#populateBean(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition, org.springframework.beans.BeanWrapper)
 * 方法的如下逻辑
 * if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
 *     for (BeanPostProcessor bp : getBeanPostProcessors()) {
 *         if (bp instanceof InstantiationAwareBeanPostProcessor) {
 *             InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
 *             if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
 *                 return;
 *             }
 *         }
 *     }
 * }
 *
 * 因为populateBean方法的作用就是完成bean的依赖注入，
 * 若上述方法中return了。那么@Autowired注解就失效了
 *
 */
public class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return false;
    }
}
