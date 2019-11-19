package com.eugene.sumarry.aop.byAnnotation.dao;

import com.eugene.sumarry.aop.byAnnotation.annotation.AspectWithin;
import org.springframework.stereotype.Repository;

@Repository
@AspectWithin
public class WithinDao {

    public void testWithinAnnotation() {
        System.out.println("test @within pointcut");
    }

    /**
     * 该方法被增强了, 对方法里面的变量 加了90
     * @param id
     */
    public void testArountJoinpoint(Long id) {
        System.out.println(id);
    }
}
