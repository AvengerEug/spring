package com.eugene.sumarry.implementmapperscan.beans;

import com.eugene.sumarry.implementmapperscan.anno.Select;
import com.eugene.sumarry.implementmapperscan.dao.UserDao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class UserDaoFactoryBean implements FactoryBean<UserDao>, InvocationHandler, ApplicationContextAware {


    private Class<UserDao> userDaoClass;

    private ApplicationContext applicationContext;

    public UserDaoFactoryBean(Class<UserDao> userDaoClass) {
        this.userDaoClass = userDaoClass;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("生成代理对象");
        System.out.println(method.getName());

        // 获取代理对象实现接口的方法, 并根据方法获取@Select注解中的值
        Select select = proxy.getClass().getInterfaces()[0].getMethod(method.getName()).getAnnotation(Select.class);
        System.out.println(select != null ? select.value() + " 获取到sql后就能查询db了" : "");

        // 此处不能return method.invoke(applicationContext.getBean(userDaoClass), args) 会死循环, 原因待确认!
        return null;
    }

    @Override
    public UserDao getObject() throws Exception {
        return (UserDao) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {
                userDaoClass
        }, this);
    }

    @Override
    public Class<?> getObjectType() {
        return userDaoClass;
    }
}
