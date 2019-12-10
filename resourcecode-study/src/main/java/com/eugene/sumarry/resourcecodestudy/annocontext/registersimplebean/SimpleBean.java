package com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(
        value = "com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean",
        nameGenerator = MyBeanNameGenerate.class)
@Import(MyImportSelector.class)
public class SimpleBean {
}
