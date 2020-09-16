package com.eugene.sumarry.resourcecodestudy.asyncannotation;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {


    public void test() {
        asyncExec();
        System.out.println("test");
    }

    @Async
    public void asyncExec() {
        System.out.println("异步执行");
    }
}
