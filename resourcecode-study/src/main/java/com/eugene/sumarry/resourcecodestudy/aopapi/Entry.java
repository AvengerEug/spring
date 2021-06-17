package com.eugene.sumarry.resourcecodestudy.aopapi;

import com.eugene.sumarry.resourcecodestudy.aopapi.advice.afteradvice.MyAfterAdvice;
import com.eugene.sumarry.resourcecodestudy.aopapi.advice.aroundadvice.MyAroundAdvice;
import com.eugene.sumarry.resourcecodestudy.aopapi.advice.beforeadvice.MyBeforeAdvice;
import com.eugene.sumarry.resourcecodestudy.aopapi.advice.introductionadvice.MyIntroductionAdvice;
import com.eugene.sumarry.resourcecodestudy.aopapi.config.AppConfigAutoProxyByBeanNameAutoProxyCreator;
import com.eugene.sumarry.resourcecodestudy.aopapi.config.AppConfigAutoProxyByDefaultAdvisorAutoProxyCreator;
import com.eugene.sumarry.resourcecodestudy.aopapi.config.AppConfigByProxyFactoryBean;
import com.eugene.sumarry.resourcecodestudy.aopapi.pointcut.MyPointcut;
import com.eugene.sumarry.resourcecodestudy.newissue.TargetService;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * spring提供的三种代理方式
 * 1、ProxyFactory
 * 2、ProxyFactoryBean
 * 3、Auto-proxy
 *   3.1）、BeanNameAutoProxyCreator
 *   3.2）、DefaultAdvisorAutoProxyCreator
 *   3.3）、AnnotationAwareAspectJAutoProxyCreator
 *
 * 核心代码： createAopProxy().getProxy()
 *
 */
public class Entry {

    public static void main(String[] args) {
        byProxyFactory();
//        byProxyFactoryBean();
//        autoProxyByBeanNameAutoProxyCreator();
//        autoProxyByDefaultAdvisorAutoProxyCreator();
//        autoProxyByAnnotationAwareAspectJAutoProxyCreator();
    }

    private static void byProxyFactory() {
        ProxyFactory proxyFactory = new ProxyFactory();

        // 将切面与切点绑定，在切点上绑定了环绕通知, 同时切点处定义了不对toString方法进行增强
        // 因此，针对于环绕通知而言，toString方法不会增强
        Advisor advisor = new DefaultPointcutAdvisor(new MyPointcut(), new MyAroundAdvice());

        // 将切面(环绕通知)添加到代理工厂中  ----> 使用此方式可以指定哪些方法不被增强
        proxyFactory.addAdvisor(advisor);

        // 添加了目标方法执行前的通知
        proxyFactory.addAdvice(new MyBeforeAdvice());

        // 添加了目标方法return后的通知
        proxyFactory.addAdvice(new MyAfterAdvice());

        // TODO 为代理类引入一个新的需要实现的接口--Runnable  ---> 动态
        proxyFactory.addAdvice(new MyIntroductionAdvice());

        // 设置目标类
        proxyFactory.setTarget(new TargetService());

        // 使用cglib代理，因为目标类没有实现接口
        proxyFactory.setProxyTargetClass(true);

        // 创建代理对象
        TargetService proxy = (TargetService) proxyFactory.getProxy();

        proxy.toString();
        System.out.println("********************************");
        proxy.testAopApi();
        System.out.println("********************************");

        // 判断引入是否成功，并执行引入的逻辑
        if (proxy instanceof Runnable) {
            new Thread(((Runnable) proxy)).start();
        }
    }

    private static void byProxyFactoryBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfigByProxyFactoryBean.class);
        // 这里要根据名字来获取，不能根据class来获取，因为spring底层会根据class来获取beanName，最终会获取到两个
        // 分别是targetService和proxyFactoryBean, spring针对根据class来获取bean的方式获取到多个beanName时
        // 会有两种解决方案：
        // 1. 返回主要的bean，也就是加了@Primary注解的bean
        // 2. 根据@Priority注解来标识，获取到两个bean，并根据两个bean的priority的值来比较，小的优先级高，
        //   TODO spring好像有bug，因为如果我有一个类是通过factoryBean代理出来的，此时获取出来的bean都是相同的，一个是目标对象，另外一个是
        //   TODO 代理对象，此时都能获取到他们的@Priority注解，所以值都一样，此时spring认为是相同的。其实不然，我们并没有给代理对象做priority的值
        //   TODO 希望有一个方式来修改代理对象的值，为ProxyFactoryBean对象添加个priority的属性，然后根据这个属性来aop生成@Priority注解，并把值填充进去
//        TargetService service = (TargetService) context.getBean(TargetService.class);

        /**
         * @see com.eugene.sumarry.resourcecodestudy.aopapi.config.AppConfigByProxyFactoryBean 的proxyFactoryBean方法
         */
        TargetService service = (TargetService) context.getBean("proxyFactoryBean");
        service.testAopApi();
        System.out.println("********************************分割线********************************");
        service.toString();
    }

    public static void autoProxyByBeanNameAutoProxyCreator() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfigAutoProxyByBeanNameAutoProxyCreator.class);
        TargetService bean = context.getBean(TargetService.class);
        bean.testAopApi();
        System.out.println("********************************分割线********************************");
        bean.toString();
    }

    public static void autoProxyByDefaultAdvisorAutoProxyCreator() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfigAutoProxyByDefaultAdvisorAutoProxyCreator.class);
        TargetService bean = context.getBean(TargetService.class);
        bean.testAopApi();
        System.out.println("********************************分割线********************************");
        bean.toString();
    }

    public static void autoProxyByAnnotationAwareAspectJAutoProxyCreator() {
        System.out.println("通过AnnotationAwareAspectJAutoProxyCreator来创建的aop就是我们平常所说的，这里不做演示了");
    }
}
