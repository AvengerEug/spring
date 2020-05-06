package com.eugene.sumarry.resourcecodestudy.circularreference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class A {

    @Autowired
    B b;
}
