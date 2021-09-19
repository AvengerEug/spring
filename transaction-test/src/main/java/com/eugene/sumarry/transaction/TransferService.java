package com.eugene.sumarry.transaction;

import java.math.BigDecimal;

public interface TransferService {

    // 转账操作
    void transfer(String outAccountId, String inAccountId, BigDecimal amount);

    // 加钱操作
    void incrementAmount(String accountId, BigDecimal amount);

    // 扣钱操作
    void decrementAmount(String accountId, BigDecimal amount);

}
