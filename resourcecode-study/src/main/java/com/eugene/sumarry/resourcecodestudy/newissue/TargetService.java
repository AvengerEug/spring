package com.eugene.sumarry.resourcecodestudy.newissue;

import org.springframework.context.annotation.Primary;

import javax.annotation.Priority;

@Primary
public class TargetService {

    public void testAopApi() {
        System.out.println("testAopApi has invoked");
    }
}
