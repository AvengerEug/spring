package com.eugene.sumarry.aop.studyannotation;

public class Test {

    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setUserName("userName");
        System.out.println(BuildUtils.buildEntitySql(user));
    }
}
