package com.eugene.sumarry.mybatis.transcationmanager;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

public class MyTransactionManager implements PlatformTransactionManager {

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        SimpleTransactionStatus simpleTransactionStatus = new SimpleTransactionStatus();
        return simpleTransactionStatus;
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        System.out.println("自定义事务管理器, commit 逻辑: " + status);
    }

    @Override
    public void rollback(TransactionStatus status) throws TransactionException {
        System.out.println("自定义事务管理器, rollback 逻辑: " + status);
    }
}
