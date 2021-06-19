package com.eugene.sumarry.transaction;

import java.math.BigDecimal;

public interface TransferService {

    void transfer(String outAccountId, String inAccountId, BigDecimal amount);

    void incrementAmount(String accountId, BigDecimal amount);

}
