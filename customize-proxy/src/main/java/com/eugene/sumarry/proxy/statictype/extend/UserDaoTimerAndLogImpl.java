package com.eugene.sumarry.proxy.statictype.extend;

public class UserDaoTimerAndLogImpl extends UserDaoLogImpl {

    @Override
    public void findList() {
        System.out.println("查询开始时间: " + System.currentTimeMillis());
        super.findList();
    }
}
