package com.eugene.sumarry.dynamic.mul.datasource.service.impl;

import com.eugene.sumarry.dynamic.mul.datasource.dao.Student1Dao;
import com.eugene.sumarry.dynamic.mul.datasource.model.Student1;
import com.eugene.sumarry.dynamic.mul.datasource.model.Student2;
import com.eugene.sumarry.dynamic.mul.datasource.service.Student1Service;
import com.eugene.sumarry.dynamic.mul.datasource.service.Student2Service;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Student1ServiceImpl implements Student1Service {

    @Autowired
    private Student1Dao student1Dao;

    @Autowired
    private Student2Service student2Service;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void create(Student1 student1) {
        student1Dao.insert(student1);
    }

    /**
     * 没有事务的service方法，调用其他三个带有事务的方法,
     * 其中
     * 只有createWithException方法内部会抛异常
     * 所以最终的外部方法是否抛异常并不会影响内部的调用，
     * 所有内部调用的事务关系是独立的
     * 所以最终的结果是：
     * 张三插入成功、李四插入成功、王五失败(因为方法内部抛了异常)
     *
     */
    @Override
    public void withoutAnyTransaction() {
        /**
         * 判断当前对象是否被spring代理过(即拥有事务的相关操作)
         * 答案是没有被代理过。为什么呢？ 因为在外部通过context.getBean(Student1Service.class);
         * 方法获取的Student1Service对象中只有create方法有事务相关逻辑，其他方法仅仅是进行了重写。
         *
         * 被代理过Student1Service对象的class类类似如下(spring使用的是CGLIB代理):
         *
         *
         * public class Proxy$Student1Service extends Student1Service {
         *
         *      private Student1Service student1Service;
         *
         *      @Override
         *      public void create(Student1 student1) {
         *          "获取db连接并开启事务"
         *          student1Service.create(student1);
         *          "commit或者rollback"
         *      }
         *
         *      public void withoutAnyTransaction() {
         *          student1Service.withoutAnyTransaction()
         *      }
         * }
         *
         * 所以在外部通过context.getBean(Student1Service.class)获取的Student1Service对象(Proxy$Student1Service),
         * 调用withoutAnyTransaction方法后，内部其实是目标对象Student1Service调用的withoutAnyTransaction()方法,
         *
         * 所以在目标对象Student1Service调用的withoutAnyTransaction()方法内使用this，这个时候的this是目标对象，
         * 目标对象的create方法，仅仅是有一个声明式注解而已，内部没有任何关于事务的逻辑，因为这些逻辑全部在
         * Proxy$Student1Service代理对象中
         *
         * 若此时想使用代理对象，可以使用spring上下文再获取一次Student1ServiceImpl对象
         *
         */
        System.out.println("对象this是否为spring事务的代理对象: " + AopUtils.isCglibProxy(this));
        Student1 student1 = new Student1("张三");
        this.create(student1);

        System.out.println("对象student2Service是否为spring事务的代理对象: " + AopUtils.isAopProxy(student2Service));
        Student2 student2 = new Student2("李四");
        student2Service.create(student2);

        student2.setStudentName("王五");
        student2Service.createWithException(student2);

        // 抛异常
        int x = 1 / 0;
    }

    /**
     * 因为REQUIRED事务传播的特性, 内部所有的方法将共用同一个事务，没有则开启
     * 只要内部有方法抛了异常，则回滚(就算try catch住了也会回滚)
     *
     * 所以最终的结果是: 张三、李四、王五全部都未插入成功
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void withRequiredTransaction() {
        System.out.println("对象this是否为spring事务的代理对象: " + AopUtils.isCglibProxy(this));
        Student1 student1 = new Student1("张三");
        this.create(student1);

        System.out.println("对象student2Service是否为spring事务的代理对象: " + AopUtils.isAopProxy(student2Service));
        Student2 student2 = new Student2("李四");
        student2Service.create(student2);

        student2.setStudentName("王五");
        try {
            student2Service.createWithException(student2);
        } catch (Exception e) {
            System.out.println("try catch住了，也会回滚");
        }
    }

    /**
     * 根据NOT_SUPPORTED事务传播机制，表示当前内部的所有方法的
     * 事务都是独立的，使用的是自己的事务。
     *
     * 所以结果是:
     * 张三插入成功、李四插入成功、王五插入失败(因为createWithException方法内部抛了异常)
     *
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void withNotSupportedTransaction() {

        System.out.println("对象this是否为spring事务的代理对象: " + AopUtils.isCglibProxy(this));
        Student1 student1 = new Student1("张三");
        this.create(student1);

        System.out.println("对象student2Service是否为spring事务的代理对象: " + AopUtils.isAopProxy(student2Service));
        Student2 student2 = new Student2("李四");
        student2Service.create(student2);

        student2.setStudentName("王五");
        student2Service.createWithException(student2);

        throw new RuntimeException();
    }

    /**
     * 根据REQUIRES_NEW的事务传播特性，内部
     * 的所有自带的事务都会走自己的逻辑，且会让上一个事务挂起。
     * 事务之间互相独立。和NOT_SUPPORTED类似
     *
     * 所以最终的结果是: 张三插入成功、李四插入成功
     *
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void withRequiresNewTransaction() {
        System.out.println("对象this是否为spring事务的代理对象: " + AopUtils.isCglibProxy(this));
        Student1 student1 = new Student1("张三");
        this.create(student1);

        System.out.println("对象student2Service是否为spring事务的代理对象: " + AopUtils.isAopProxy(student2Service));
        Student2 student2 = new Student2("李四");
        student2Service.create(student2);

        // 外部方法抛异常
        int x = 10 / 0;
    }

    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void withNestedTransaction() {
        System.out.println("对象this是否为spring事务的代理对象: " + AopUtils.isCglibProxy(this));
        Student1 student1 = new Student1("张三");
        this.create(student1);

        System.out.println("对象student2Service是否为spring事务的代理对象: " + AopUtils.isAopProxy(student2Service));
        Student2 student2 = new Student2("李四");
        student2Service.create(student2);

        student2.setStudentName("王五");
        try {
            student2Service.createWithException(student2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
