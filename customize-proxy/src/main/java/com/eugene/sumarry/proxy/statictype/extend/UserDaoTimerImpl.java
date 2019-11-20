package com.eugene.sumarry.proxy.statictype.extend;

import com.eugene.sumarry.proxy.UserDaoImpl;

public class UserDaoTimerImpl extends UserDaoImpl {

    @Override
    public void findList() {
        System.out.println("查询开始时间: " + System.currentTimeMillis());
        super.findList();
    }
}
