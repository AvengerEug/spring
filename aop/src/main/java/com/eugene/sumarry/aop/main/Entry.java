package com.eugene.sumarry.aop.main;

import com.eugene.sumarry.aop.main.dao.BaseDao;
import com.eugene.sumarry.aop.main.dao.WithinDao;
import com.eugene.sumarry.aop.main.daoproxy.PrototypeDao;
import com.eugene.sumarry.aop.main.daoproxy.StudentDaoImpl;
import com.eugene.sumarry.aop.main.daoproxy.UserDao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sun.misc.ProxyGenerator;

import java.io.*;

/**
 * 横切性问题: (共同的特点, 不会影响主业务流程)
 *   1. 日志记录
 *   2. 权限验证
 *   3. api效率检查
 *   4. 事务管理
 *
 * Aspect: 切面    ---- 就是连接点, 切点, 织入, 通知的载体
 * Join Point: 连接点: 对一个方法进行增强, 这个方法就是连接点  -->  目标对象的方法  -->  类似于表中的记录
 * Pointcut: 切点:  连接点的集合   ->  类似于一张表
 * Weaving: 织入: 连接点(目标对象的方法)增强的过程
 * Advice: 通知: 什么时候通知, 通知到哪里去, 就是增强的部分要放在哪里去
 * target: 原始对象(目标对象)
 * proxy: 代理对象(增强后的对象, 包含了原始对象和增强后的代码)
 */
public class Entry {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        BaseDao baseDao = context.getBean(BaseDao.class);
        System.out.println("====================== findList(s)\n");
        baseDao.findList("s");
        System.out.println("====================== findList(1)\n");
        baseDao.findList(1);

        System.out.println("====================== findList(\"1\", 1)\n");
        baseDao.findList("1", 1);

        System.out.println("====================== testAnnotationExecution()\n");
        baseDao.testAnnotationExecution();

        System.out.println("====================== testArgsAnnotationExecution()\n");
        baseDao.testArgsAnnotationExecution("eugene");

        System.out.println("====================== testWithinAnnotation()\n");
        WithinDao withinDao = context.getBean(WithinDao.class);
        withinDao.testWithinAnnotation();

        System.out.println("====================== testArountJoinpoint()\n");
        withinDao.testArountJoinpoint(1L);

        System.out.println("--------------- proxy type-- findList -----------------\n");
        UserDao userDao = (UserDao) context.getBean("userDaoImpl");
        userDao.findList();

        System.out.println("--------------- proxy type-- findById -----------------\n");
        userDao.findById(1);

        System.out.println("*************** 使用jdk源码生成代理对象的class文件内容 *********");
        // 生成的代理对象是一个字节码文件, 其实就是一串字节数组, 把它写成class文件反编译后就能看到内容了
        // 可以看到的内容是 生成的代理对象的类UserDaoTarget是继承Proxy且实现UserDao的
        byte classByte[] = ProxyGenerator.generateProxyClass("UserDaoTarget", new Class[] {UserDao.class});
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File("c:\\UserDaoTarget.class"));
            os.write(classByte);
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("***************** 强制将某个类(StudentDaoImpl)变成其他类型********************");
        UserDao userDao2 = (UserDao) context.getBean("studentDaoImpl");
        userDao2.findList();

        System.out.println("***************** 测试原型对象产生的代理对象是否也为原型的 ********************");
        PrototypeDao prototypeDao1 = context.getBean(PrototypeDao.class);
        PrototypeDao prototypeDao2 = context.getBean(PrototypeDao.class);
        prototypeDao1.testPrototypeAspect();
        // 每执行一次增强的方法, 代理对象都是新的
        prototypeDao2.testPrototypeAspect();
    }
}
