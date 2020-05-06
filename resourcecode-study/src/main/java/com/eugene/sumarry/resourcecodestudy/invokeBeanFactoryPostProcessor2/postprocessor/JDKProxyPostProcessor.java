package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.postprocessor;

import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.proxy.MyInvocationHandler;
import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.proxy.ProxyUtil;
import com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.service.impl.UserServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 动态代理后置处理器, 这里只为userServiceImpl创建代理对象
 */
public class JDKProxyPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean,
        String beanName) throws BeansException {

        if (beanName.equals("userServiceImpl")) {

            return ProxyUtil.newInstance(UserServiceImpl.class.getInterfaces(),
                    new MyInvocationHandler(bean));
        }
        return bean;
    }
}
