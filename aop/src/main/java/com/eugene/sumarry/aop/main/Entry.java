package com.eugene.sumarry.aop.main;

import com.eugene.sumarry.aop.main.dao.BaseDao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
        baseDao.findList();
    }
}
