package com.eugene.sumarry.transaction;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class TransferServiceImpl implements TransferService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void transfer(String outAccountId, String inAccountId, BigDecimal amount) {
        // 进钱
        ((TransferService) AopContext.currentProxy()).incrementAmount(inAccountId, amount);

        // ...... 可以增加扩展代码  @1

        // 出钱
        jdbcTemplate.update("UPDATE account SET amount = amount - ? WHERE id = ?", amount, outAccountId);

        // ...... 可以增加扩展代码  @2
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW) // @3
    @Override
    public void incrementAmount(String accountId, BigDecimal amount) {
        // 不考虑任何并发情况，直接新增金额
        jdbcTemplate.update("UPDATE account SET amount = amount + ? WHERE id = ?", amount, accountId);

        // ...... 可以增加扩展代码  @4
    }
}
