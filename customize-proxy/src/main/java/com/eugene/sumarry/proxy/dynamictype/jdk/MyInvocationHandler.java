package com.eugene.sumarry.proxy.dynamictype.jdk;

import java.lang.reflect.Method;

public interface MyInvocationHandler {

    /**
     *
     * @param method 目标对象的方法
     * @param args 执行目标对象方法的参数
     * @return
     */
    Object invoke(Method method, Object ...args);

}
