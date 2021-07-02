package com.eugene.sumarry.mybatis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class TransactionLifecyclePostProcessor {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void test() {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            executor.submit(() -> {
            });
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_COMMITTED) {
                    System.out.println("事务提交后再执行");
                    executor.submit(() -> {
                        // 发送消息给kafka
                    });
                }
            }

        });

    }
}
