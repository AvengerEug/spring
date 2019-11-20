package com.eugene.sumarry.proxy.statictype.extend;

public class UserDaoLogAndTimerImpl extends UserDaoTimerImpl {

    @Override
    public void findList() {
        System.out.println("[USER MODULE]查询用户数据 开始");
        super.findList();
    }
}
