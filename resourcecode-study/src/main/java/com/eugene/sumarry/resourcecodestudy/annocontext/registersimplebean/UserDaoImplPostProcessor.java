package com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Proxy;

public class UserDaoImplPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("userDaoImplEugene")) {
            bean = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {
                    UserDao.class
            }, new MyInvocationHandler(bean));
        }

        return bean;
    }
}
