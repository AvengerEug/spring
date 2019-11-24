package com.eugene.sumarry.resourcecodestudy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public class TestBeanFactory implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        return new TestBean();
    }

    @Override
    public Class<?> getObjectType() {
        return TestBean.class;
    }
}
