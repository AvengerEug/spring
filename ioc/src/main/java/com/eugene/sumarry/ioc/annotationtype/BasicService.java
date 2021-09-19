package com.eugene.sumarry.ioc.annotationtype;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 新增构造方法, 带参和不带参的构造方法。
 * 在使用的地方使用@Lookup修饰方法, 如下:
 *     @Lookup
 *     public abstract BasicService getBasicService(String userName);
 *
 *     @Lookups
 *     public abstract BasicService getBasicService();
 *
 *     当调用带参数的方法时, 会调用带参数的构造方法. 以此类推
 */
@Component
@Scope("prototype")
public class BasicService {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BasicService(String userName) {
        this.userName = userName;
    }

    public BasicService() {}
}
