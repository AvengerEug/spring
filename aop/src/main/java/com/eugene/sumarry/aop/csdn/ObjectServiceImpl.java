package com.eugene.sumarry.aop.csdn;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

@Service
public class ObjectServiceImpl implements ObjectService {

    @Entry.AspectAnnotation
    @Override
    public String[] list(String str) {
        ((ObjectService) AopContext.currentProxy()).findOne();
        return new String[] {str, "avenger", "eug"};
    }

    @Entry.AspectAnnotation
    @Override
    public String findOne() {
        return "avengerEug";
    }
}
