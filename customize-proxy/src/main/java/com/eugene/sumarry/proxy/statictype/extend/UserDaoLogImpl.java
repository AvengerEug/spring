package com.eugene.sumarry.proxy.statictype.extend;

import com.eugene.sumarry.proxy.UserDaoImpl;

public class UserDaoLogImpl extends UserDaoImpl {

    @Override
    public void findList() {
        System.out.println("[USER MODULE]查询用户数据");
        super.findList();
    }
}
