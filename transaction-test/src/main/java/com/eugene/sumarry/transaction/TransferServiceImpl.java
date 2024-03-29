package com.eugene.sumarry.transaction;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;

@Component
public class TransferServiceImpl implements TransferService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transfer(String outAccountId, String inAccountId, BigDecimal amount) {

        // 主逻辑：插入一条数据
        jdbcTemplate.execute("INSERT INTO account(id, amount) VALUES('test1', 20)");

        // 进钱
        ((TransferService) AopContext.currentProxy()).incrementAmount(inAccountId, amount);

        // ...... 可以增加扩展代码  @1

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            System.out.println("有事务");
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    if (status == TransactionSynchronization.STATUS_COMMITTED) {
                        System.out.println("事务提交后的钩子函数");
                    } else if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                        System.out.println("事务回滚");
                    }
                }
            });
        } else {
            System.out.println("无事务");
        }

        // ...... 可以增加扩展代码  @2
        // 出钱(抛异常，校验插入数据是否完成)
        ((TransferService) AopContext.currentProxy()).decrementAmount(outAccountId, amount);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED) // @3
    @Override
    public void incrementAmount(String accountId, BigDecimal amount) {
        // 不考虑任何并发情况，直接新增金额
        jdbcTemplate.update("UPDATE account SET amount = amount + ? WHERE id = ?", amount, accountId);

        // ...... 可以增加扩展代码  @4
    }

    @Transactional(rollbackFor = Exception.class) // @3
    @Override
    public void decrementAmount(String accountId, BigDecimal amount) {
        // 出钱
        jdbcTemplate.update("UPDATE account SET amount = amount - ? WHERE id = ?", amount, accountId);
        int x = 1 / 0;
    }
}
