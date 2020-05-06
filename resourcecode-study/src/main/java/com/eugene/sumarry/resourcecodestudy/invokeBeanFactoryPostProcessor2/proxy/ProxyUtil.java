package com.eugene.sumarry.resourcecodestudy.invokeBeanFactoryPostProcessor2.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 创建代理对象工具类
 */
public class ProxyUtil {

    public static Object newInstance(Class<?>[] interfaces, InvocationHandler invocationHandler) {
        return Proxy.newProxyInstance(ProxyUtil.class.getClassLoader(),
                interfaces, invocationHandler);
    }
}
