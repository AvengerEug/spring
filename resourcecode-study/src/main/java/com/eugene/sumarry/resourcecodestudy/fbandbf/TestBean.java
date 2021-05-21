package com.eugene.sumarry.resourcecodestudy.fbandbf;

import org.springframework.beans.factory.InitializingBean;

public class TestBean implements InitializingBean {

    /**
     * TestBean虽然由TestBeanFactoryBean创建的，它也位于spring容器中，
     * 但是它并没有经历spring的生命周期，因此，此方法不会被回调
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }
}
