package com.eugene.sumarry.resourcecodestudy.newissue;

import javax.annotation.Priority;

@Priority(12)
public class TargetService {

    public void testAopApi() {
        System.out.println("testAopApi has invoked");
    }
}
