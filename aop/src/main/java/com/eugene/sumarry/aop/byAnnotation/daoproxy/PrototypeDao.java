package com.eugene.sumarry.aop.byAnnotation.daoproxy;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class PrototypeDao {

    public void testPrototypeAspect() {
        System.out.println("///////////////testPrototypeAspect 测试原型对象的代理对象是否也为prototype");
    }
}
