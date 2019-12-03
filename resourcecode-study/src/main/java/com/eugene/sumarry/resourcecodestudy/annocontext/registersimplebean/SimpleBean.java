package com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        value = "com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean",
        nameGenerator = MyBeanNameGenerate.class)
public class SimpleBean {
}
