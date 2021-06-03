package com.eugene.sumarry.mybatis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class TransactionLifecyclePostProcessor {


    public void test() {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            System.out.println("无事务，直接执行");
            return;
        } else {

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

                @Override
                public void afterCompletion(int status) {
                    // 事务提交后，记录审计
                    if (status == TransactionSynchronization.STATUS_COMMITTED) {
                        System.out.println("事务提交后再执行");
                    }
                }

            });
        }

    }

}
