package com.eugene.sumarry.aop.main.dao;

import com.eugene.sumarry.aop.main.annotation.AspwctWithin;
import org.springframework.stereotype.Repository;

@Repository
@AspwctWithin
public class WithinDao {

    public void testWithinAnnotation() {
        System.out.println("test @within pointcut");
    }
}
