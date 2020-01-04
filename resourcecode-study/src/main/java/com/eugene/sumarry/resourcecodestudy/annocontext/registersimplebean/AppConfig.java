package com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(
        value = "com.eugene.sumarry.resourcecodestudy.annocontext.registersimplebean",
        nameGenerator = MyBeanNameGenerate.class)
@Import(MyImportSelector.class)
public class AppConfig {


    /*@Bean
    public TestBeanAnnotation1 testBeanAnnotation1() {
        System.out.println(">>>>>>>>>>>>>>>>testBeanAnnotation1");
        return new TestBeanAnnotation1();
    }

    @Bean
    public TestBeanAnnotation2 testBeanAnnotation2() {
        testBeanAnnotation1();
        testBeanAnnotation1();
        return new TestBeanAnnotation2();
    }*/

}
