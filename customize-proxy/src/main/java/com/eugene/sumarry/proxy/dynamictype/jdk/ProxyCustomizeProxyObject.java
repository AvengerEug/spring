package com.eugene.sumarry.proxy.dynamictype.jdk;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 其实这个类就是增强代理类的代理类,
 *
 * 因为上一个手写动态代理的版本中, 增强的业务逻辑是写死的。
 * 要想把增强的业务逻辑也动态化, 那就把代理对象再代理一遍, 这就是此类产生的原由
 */
public class ProxyCustomizeProxyObject implements MyInvocationHandler {

    /**
     * 这个对象就是目标对象, 要实现方法的自定义, 就是在invoke中自定义一些方法
     */
    private Object target;

    public ProxyCustomizeProxyObject(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Method method, Object ...args) {
        try {
            System.out.println("自定义增强方法 before");
            Object result = method.invoke(target, args);
            System.out.println("自定义增强方法 after");
            return result;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
