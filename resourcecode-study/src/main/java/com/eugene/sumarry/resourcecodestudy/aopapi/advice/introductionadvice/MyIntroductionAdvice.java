package com.eugene.sumarry.resourcecodestudy.aopapi.advice.introductionadvice;

import org.springframework.aop.support.DelegatingIntroductionInterceptor;

/**
 * 引入通知，可以让代理类实现指定的接口，
 * eg：在本引入通知中实现了Runnable接口，因此代理类也会实现Runnable接口
 */
public class MyIntroductionAdvice extends DelegatingIntroductionInterceptor implements Runnable {

    @Override
    public void run() {
        System.out.println("running");
    }
}
